package ch.cern.todo.controller;

import ch.cern.todo.model.api.CreateTaskCategoryApi;
import ch.cern.todo.model.api.TaskCategoryApi;
import ch.cern.todo.model.api.UpdateTaskCategoryApi;
import ch.cern.todo.model.business.TaskCategory;
import ch.cern.todo.model.mapper.TaskCategoriesMapper;
import ch.cern.todo.service.TaskCategoriesService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskCategoriesController {

    private final TaskCategoriesMapper taskCategoriesMapper;
    private final TaskCategoriesService taskCategoriesService;

    @GetMapping("/v1/task-category/{categoryId}")
    public ResponseEntity<TaskCategoryApi> getTaskCategory(@PathVariable Long categoryId) {
        val result = taskCategoriesService.getTaskCategory(categoryId);

        val apiResult = taskCategoriesMapper.businessToApi(result);

        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/v1/task-category")
    public ResponseEntity<TaskCategoryApi> createTaskCategory(@Validated @RequestBody CreateTaskCategoryApi body) {
        val input = TaskCategory.builder()
            .categoryName(body.getCategoryName())
            .categoryDescription(body.getCategoryDescription())
            .build();

        val result = taskCategoriesService.createTaskCategory(input);

        val apiResult = taskCategoriesMapper.businessToApi(result);

        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

    @PutMapping("/v1/task-category/{categoryId}")
    public ResponseEntity<TaskCategoryApi> updateTaskCategory(@PathVariable Long categoryId,
                                                              @Validated @RequestBody UpdateTaskCategoryApi body) {
        val input = TaskCategory.builder()
            .categoryId(categoryId)
            .categoryName(body.getCategoryName())
            .categoryDescription(body.getCategoryDescription())
            .build();

        val result = taskCategoriesService.updateTaskCategory(input);

        val apiResult = taskCategoriesMapper.businessToApi(result);

        return ResponseEntity.ok(apiResult);
    }


    @DeleteMapping("/v1/task-category/{categoryId}")
    public ResponseEntity<Void> deleteTaskCategory(@PathVariable Long categoryId) {
        taskCategoriesService.deleteTaskCategory(categoryId);

        return ResponseEntity.noContent().build();
    }
}
