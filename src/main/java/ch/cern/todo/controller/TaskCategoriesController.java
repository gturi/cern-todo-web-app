package ch.cern.todo.controller;

import ch.cern.todo.model.api.CreateTaskCategoryApi;
import ch.cern.todo.model.api.TaskCategoryApi;
import ch.cern.todo.model.business.TaskCategory;
import ch.cern.todo.model.mapper.TaskCategoriesMapper;
import ch.cern.todo.service.TaskCategoriesService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskCategoriesController {

    private final TaskCategoriesMapper taskCategoriesMapper;
    private final TaskCategoriesService taskCategoriesService;

    @PostMapping("/task-category")
    public ResponseEntity<TaskCategoryApi> createTaskCategory(@Validated @RequestBody CreateTaskCategoryApi body) {
        val input = TaskCategory.builder()
            .categoryName(body.getCategoryName())
            .categoryDescription(body.getCategoryDescription())
            .build();

        val result = taskCategoriesService.createTaskCategory(input);

        val apiResult = taskCategoriesMapper.businessToApi(result);

        return ResponseEntity.ok(apiResult);
    }
}
