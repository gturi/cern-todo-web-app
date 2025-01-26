package ch.cern.todo.service;

import ch.cern.todo.model.business.CernPage;
import ch.cern.todo.model.business.LoggedUserInfo;
import ch.cern.todo.model.business.SearchTask;
import ch.cern.todo.model.business.Task;
import ch.cern.todo.model.mapper.TaskMapper;
import ch.cern.todo.repository.TaskCategoriesRepository;
import ch.cern.todo.repository.TaskRepository;
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

    public Task getTask(Long taskId) {
        val taskEntity = taskRepository.getTaskById(taskId);

        // TODO: throw 404 if not found

        return taskMapper.entityToBusiness(taskEntity);
    }

    public CernPage<Task> getTasks(SearchTask searchTask) {
        val tasks = taskRepository.findTasks(searchTask);

        return tasks.map(taskMapper::entityToBusiness);
    }

    @Transactional
    public Task createTask(Task task, LoggedUserInfo loggedUserInfo) {
        val taskCategory = taskCategoriesRepository.getReferenceById(task.getCategoryId());

        val taskEntity = taskMapper.businessToEntity(task);
        taskEntity.setTaskCategory(taskCategory);
        taskEntity.setCreationUserId(loggedUserInfo.userId());
        taskEntity.setCreationUserFirstname(loggedUserInfo.firstname());
        taskEntity.setCreationUserLastname(loggedUserInfo.lastname());
        taskEntity.setUpdateUserId(loggedUserInfo.userId());
        taskEntity.setUpdateUserFirstname(loggedUserInfo.firstname());
        taskEntity.setUpdateUserLastname(loggedUserInfo.lastname());

        taskRepository.save(taskEntity);

        return taskMapper.entityToBusiness(taskEntity);
    }

    @Transactional
    public Task updateTask(Task task, LoggedUserInfo loggedUserInfo) {
        val taskEntity = taskRepository.getReferenceById(task.getTaskId());

        if (!loggedUserInfo.isAdmin() && !taskEntity.getCreationUserId().equals(loggedUserInfo.userId())) {
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

        taskEntity.setUpdateUserId(loggedUserInfo.userId());
        taskEntity.setUpdateUserFirstname(loggedUserInfo.firstname());
        taskEntity.setUpdateUserLastname(loggedUserInfo.lastname());

        taskRepository.save(taskEntity);

        return taskMapper.entityToBusiness(taskEntity);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
