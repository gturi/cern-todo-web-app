package ch.cern.todo.service;

import ch.cern.todo.model.business.CernPageable;
import ch.cern.todo.model.business.Task;
import ch.cern.todo.model.mapper.TaskMapper;
import ch.cern.todo.repository.TaskCategoriesRepository;
import ch.cern.todo.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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


    public Page<Task> getTasks(String userName, String taskName, String taskDescription, LocalDate deadline,
                               String categoryName, CernPageable pageable) {
        val tasks = taskRepository.findTasks(
            userName, taskName, taskDescription, deadline, categoryName, pageable
        );

        return tasks.map(taskMapper::entityToBusiness);
    }

    @Transactional
    public Task createTask(Task task) {
        val taskCategory = taskCategoriesRepository.getReferenceById(task.getCategoryId());

        val taskEntity = taskMapper.businessToEntity(task);
        taskEntity.setTaskCategory(taskCategory);

        taskRepository.save(taskEntity);

        return taskMapper.entityToBusiness(taskEntity);
    }

    @Transactional
    public Task updateTask(Task task) {
        val taskEntity = taskRepository.getReferenceById(task.getTaskId());

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

        taskRepository.save(taskEntity);

        return taskMapper.entityToBusiness(taskEntity);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
