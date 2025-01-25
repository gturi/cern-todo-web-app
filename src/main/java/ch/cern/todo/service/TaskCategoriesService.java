package ch.cern.todo.service;

import ch.cern.todo.model.business.TaskCategory;
import ch.cern.todo.model.mapper.TaskCategoriesMapper;
import ch.cern.todo.repository.TaskCategoriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskCategoriesService {

    private final TaskCategoriesMapper taskCategoriesMapper;
    private final TaskCategoriesRepository taskCategoriesRepository;

    public TaskCategory createTaskCategory(TaskCategory taskCategory) {
        val taskCategoryEntity = taskCategoriesMapper.businessToEntity(taskCategory);

        taskCategoriesRepository.save(taskCategoryEntity);

        return taskCategoriesMapper.entityToBusiness(taskCategoryEntity);
    }
}
