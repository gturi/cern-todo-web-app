package ch.cern.todo.service;

import ch.cern.todo.model.business.TaskCategory;
import ch.cern.todo.model.mapper.TaskCategoriesMapper;
import ch.cern.todo.repository.TaskCategoriesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskCategoriesService {

    private final TaskCategoriesMapper taskCategoriesMapper;
    private final TaskCategoriesRepository taskCategoriesRepository;

    public TaskCategory getTaskCategory(Long categoryId) {
        return taskCategoriesRepository.findById(categoryId)
            .map(taskCategoriesMapper::entityToBusiness)
            .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public TaskCategory createTaskCategory(TaskCategory taskCategory) {
        val taskCategoryEntity = taskCategoriesMapper.businessToEntity(taskCategory);

        taskCategoriesRepository.save(taskCategoryEntity);

        return taskCategoriesMapper.entityToBusiness(taskCategoryEntity);
    }

    @Transactional
    public TaskCategory updateTaskCategory(TaskCategory taskCategory) {
        val taskCategoryEntity = taskCategoriesRepository.getReferenceById(taskCategory.getCategoryId());

        if (StringUtils.isNotBlank(taskCategory.getCategoryName())) {
            taskCategoryEntity.setCategoryName(taskCategory.getCategoryName());
        }
        if (StringUtils.isNotBlank(taskCategory.getCategoryDescription())) {
            taskCategoryEntity.setCategoryDescription(taskCategory.getCategoryDescription());
        }

        taskCategoriesRepository.save(taskCategoryEntity);

        return taskCategoriesMapper.entityToBusiness(taskCategoryEntity);
    }
}
