package ch.cern.todo.service;

import ch.cern.todo.model.business.CernException;
import ch.cern.todo.model.business.TaskCategory;
import ch.cern.todo.model.database.TaskCategoryEntity;
import ch.cern.todo.model.mapper.TaskCategoriesMapper;
import ch.cern.todo.repository.TaskCategoriesRepository;
import ch.cern.todo.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskCategoriesService {

    private final TaskCategoriesMapper taskCategoriesMapper;
    private final TaskCategoriesRepository taskCategoriesRepository;
    private final TaskRepository taskRepository;

    public TaskCategory getTaskCategory(Long categoryId) {
        val taskCategoryEntity = getTaskCategoryEntity(categoryId);

        return taskCategoriesMapper.entityToBusiness(taskCategoryEntity);
    }

    @Transactional
    public TaskCategory createTaskCategory(TaskCategory taskCategory) {
        val taskCategoryEntity = taskCategoriesMapper.businessToEntity(taskCategory);

        taskCategoriesRepository.save(taskCategoryEntity);

        return taskCategoriesMapper.entityToBusiness(taskCategoryEntity);
    }

    @Transactional
    public TaskCategory updateTaskCategory(TaskCategory taskCategory) {
        val taskCategoryEntity = getTaskCategoryEntity(taskCategory.getCategoryId());

        if (StringUtils.isNotBlank(taskCategory.getCategoryName())) {
            taskCategoryEntity.setCategoryName(taskCategory.getCategoryName());
        }
        if (StringUtils.isNotBlank(taskCategory.getCategoryDescription())) {
            taskCategoryEntity.setCategoryDescription(taskCategory.getCategoryDescription());
        }

        taskCategoriesRepository.save(taskCategoryEntity);

        return taskCategoriesMapper.entityToBusiness(taskCategoryEntity);
    }

    @Transactional
    public void deleteTaskCategory(Long categoryId) {
        val taskCategoryEntity = getTaskCategoryEntity(categoryId);

        if (taskRepository.existsTaskAssociatedToCategory(categoryId)) {
            throw new CernException(
                "Cannot delete task category since it is associated to a task", HttpStatus.BAD_REQUEST
            );
        }

        taskCategoriesRepository.delete(taskCategoryEntity);
    }

    private TaskCategoryEntity getTaskCategoryEntity(Long categoryId) {
        return taskCategoriesRepository.findById(categoryId)
            .orElseThrow(() -> new CernException("Task category not found", HttpStatus.NOT_FOUND));
    }
}
