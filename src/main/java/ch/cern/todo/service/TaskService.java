package ch.cern.todo.service;

import ch.cern.todo.model.business.CernPage;
import ch.cern.todo.model.business.LoggedUserInfo;
import ch.cern.todo.model.business.SearchTask;
import ch.cern.todo.model.business.Task;
import ch.cern.todo.model.mapper.TaskMapper;
import ch.cern.todo.repository.TaskCategoriesRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
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

    public Task getTask(Long taskId, LoggedUserInfo loggedUserInfo) {
        val taskEntity = taskRepository.getTaskById(taskId);

        // TODO: throw 404 if not found

        // TODO: If security is a concern and we want to avoid exposing unnecessary information,
        //  the exception should be a generic 404. In that case, loggedUserInfo.userId() could be added as
        //  a filter of getTaskById() paramethers
        if (!loggedUserInfo.isAdmin() && !taskEntity.getAssignedToUserId().equals(loggedUserInfo.userId())) {
            throw new RuntimeException("You are not allowed to see this task"); // TODO: custom exception
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

        val taskCategory = taskCategoriesRepository.getReferenceById(task.getCategoryId());
        val user = userRepository.getReferenceById(task.getAssignedToUserInfo().getUserId());

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
        val taskEntity = taskRepository.getReferenceById(task.getTaskId());

        if (!canModifyTask(taskEntity.getCreationUserId(), loggedUserInfo)) {
            throw new RuntimeException("You are not allowed to update this task"); // TODO: custom exception
        }

        if (StringUtils.isNotBlank(task.getTaskName())) {
            taskEntity.setTaskName(task.getTaskName());
        }

        if (StringUtils.isNotBlank(task.getTaskDescription())) {
            taskEntity.setTaskDescription(task.getTaskDescription());
        }

        Optional.ofNullable(task.getDeadline()).ifPresent(taskEntity::setDeadline);

        if (task.getCategoryId() != null && !task.getCategoryId().equals(taskEntity.getCategoryId())) {
            val taskCategory = taskCategoriesRepository.getReferenceById(task.getCategoryId());
            taskEntity.setCategoryId(task.getCategoryId());
            taskEntity.setTaskCategory(taskCategory);
        }

        if (task.getAssignedToUserInfo().getUserId() != null
            && !task.getAssignedToUserInfo().getUserId().equals(taskEntity.getAssignedToUserId())) {
            val userEntity = userRepository.getReferenceById(task.getAssignedToUserInfo().getUserId());
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
            throw new RuntimeException("You are not allowed to delete this task"); // TODO: custom exception
        }
        taskRepository.deleteById(taskId);
    }

    private boolean canModifyTask(Long creatorUserId, LoggedUserInfo loggedUserInfo) {
        // TODO: who can update a task? The creator, the assignee, the admin? -> let's assume the creator and the admin
        return loggedUserInfo.isAdmin() || creatorUserId.equals(loggedUserInfo.userId());
    }
}
