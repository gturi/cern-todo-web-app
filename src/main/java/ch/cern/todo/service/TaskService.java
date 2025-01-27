package ch.cern.todo.service;

import ch.cern.todo.model.business.CernException;
import ch.cern.todo.model.business.CernPage;
import ch.cern.todo.model.business.LoggedUserInfo;
import ch.cern.todo.model.business.SearchTask;
import ch.cern.todo.model.business.Task;
import ch.cern.todo.model.database.TaskCategoryEntity;
import ch.cern.todo.model.database.UserEntity;
import ch.cern.todo.model.mapper.TaskMapper;
import ch.cern.todo.repository.TaskCategoriesRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final TaskCategoriesRepository taskCategoriesRepository;
    private final UserRepository userRepository;
    private final TaskCategoriesService taskCategoriesService;

    public Task getTask(Long taskId, LoggedUserInfo loggedUserInfo) {
        val taskEntity = taskRepository.getTaskById(taskId);

        if (taskEntity == null) {
            throw new CernException("Task not found", HttpStatus.NOT_FOUND);
        }

        // TODO: If security is a concern and we want to avoid exposing unnecessary information,
        //  the exception should be a generic 404. In that case, loggedUserInfo.userId() could be added as
        //  a filter of getTaskById() paramethers
        if (!loggedUserInfo.isAdmin() && !taskEntity.getAssignedToUserId().equals(loggedUserInfo.userId())) {
            throw new CernException("You are not allowed to see this task", HttpStatus.UNAUTHORIZED);
        }

        return taskMapper.entityToBusiness(taskEntity);
    }

    public CernPage<Task> getTasks(SearchTask searchTask, LoggedUserInfo loggedUserInfo) {
        val tasks = taskRepository.findTasks(searchTask, loggedUserInfo);

        return tasks.map(taskMapper::entityToBusiness);
    }

    @Transactional
    public Task createTask(Task task, LoggedUserInfo loggedUserInfo) {
        // TODO: strange behaviour due to the lack of detailed specification:
        //  a user can create a task for another user, but after that, if he is not admin, he will not be able to see it
        if (task.getAssignedToUserInfo().getUserId() == null) {
            throw new CernException("Assigned to user is required", HttpStatus.BAD_REQUEST);
        }

        val taskCategory = getTaskCategoryEntity(task.getCategoryId());

        val user = getUserEntity(task.getAssignedToUserInfo().getUserId());

        val taskEntity = taskMapper.businessToEntity(task);
        taskEntity.setTaskCategory(taskCategory);
        taskEntity.setCreationUserId(loggedUserInfo.userId());
        taskEntity.setUpdateUserId(loggedUserInfo.userId());
        taskEntity.setAssignedToUserId(user.getUserId());
        taskEntity.setAssignedToUsername(user.getUsername());

        taskRepository.save(taskEntity);

        return taskMapper.entityToBusiness(taskEntity);
    }

    @Transactional
    public Task updateTask(Task task, LoggedUserInfo loggedUserInfo) {
        val taskEntity = taskRepository.findById(task.getTaskId())
            .orElseThrow(() -> new CernException("Task not found", HttpStatus.NOT_FOUND));

        if (!canModifyTask(taskEntity.getCreationUserId(), loggedUserInfo)) {
            throw new CernException("You are not allowed to update this task", HttpStatus.UNAUTHORIZED);
        }

        if (StringUtils.isNotBlank(task.getTaskName())) {
            taskEntity.setTaskName(task.getTaskName());
        }

        if (StringUtils.isNotBlank(task.getTaskDescription())) {
            taskEntity.setTaskDescription(task.getTaskDescription());
        }

        Optional.ofNullable(task.getDeadline()).ifPresent(taskEntity::setDeadline);

        if (task.getCategoryId() != null && !task.getCategoryId().equals(taskEntity.getCategoryId())) {
            val taskCategory = getTaskCategoryEntity(task.getCategoryId());
            taskEntity.setCategoryId(task.getCategoryId());
            taskEntity.setTaskCategory(taskCategory);
        }

        if (task.getAssignedToUserInfo().getUserId() != null
            && !task.getAssignedToUserInfo().getUserId().equals(taskEntity.getAssignedToUserId())) {
            val userEntity = getUserEntity(task.getAssignedToUserInfo().getUserId());
            taskEntity.setAssignedToUserId(userEntity.getUserId());
            taskEntity.setAssignedToUsername(userEntity.getUsername());
        }

        taskEntity.setUpdateUserId(loggedUserInfo.userId());

        taskRepository.save(taskEntity);

        return taskMapper.entityToBusiness(taskEntity);
    }

    @Transactional
    public void deleteTask(Long taskId, LoggedUserInfo loggedUserInfo) {
        Long creatorUserId = taskRepository.getCreatorUserId(taskId);

        if (!canModifyTask(creatorUserId, loggedUserInfo)) {
            throw new CernException("You are not allowed to delete this task", HttpStatus.UNAUTHORIZED);
        }

        // TODO: at the moment hard deletion is performed,
        //  but a soft delete could be implemented to keep the data for audit purposes
        taskRepository.deleteById(taskId);
    }

    private boolean canModifyTask(Long creatorUserId, LoggedUserInfo loggedUserInfo) {
        // TODO: who can update a task? The creator, the assignee, the admin? -> let's assume the creator and the admin
        return loggedUserInfo.isAdmin() || creatorUserId.equals(loggedUserInfo.userId());
    }

    private TaskCategoryEntity getTaskCategoryEntity(Long categoryId) {
        return taskCategoriesRepository.findById(categoryId)
            .orElseThrow(() -> new CernException("Task category not found", HttpStatus.NOT_FOUND));
    }

    private UserEntity getUserEntity(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CernException("User not found", HttpStatus.NOT_FOUND));
    }
}
