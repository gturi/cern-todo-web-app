package ch.cern.todo.controller;

import ch.cern.todo.model.api.CreateTaskApi;
import ch.cern.todo.model.api.PageApi;
import ch.cern.todo.model.api.TaskApi;
import ch.cern.todo.model.api.UpdateTaskApi;
import ch.cern.todo.model.business.CernPageable;
import ch.cern.todo.model.business.SearchTask;
import ch.cern.todo.model.business.Task;
import ch.cern.todo.model.mapper.PageMapper;
import ch.cern.todo.model.mapper.TaskMapper;
import ch.cern.todo.service.TaskService;
import ch.cern.todo.util.LoggedUserUtils;
import jakarta.validation.constraints.Min;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final PageMapper pageMapper;
    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @GetMapping("/v1/task/{taskId}")
    public ResponseEntity<TaskApi> getTask(@PathVariable Long taskId) {
        val result = taskService.getTask(taskId);

        val apiResult = taskMapper.businessToApi(result);

        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/v1/task")
    public ResponseEntity<PageApi<TaskApi>> getTasks(@RequestParam(required = false) String userName,
                                                     @RequestParam(required = false) String taskName,
                                                     @RequestParam(required = false) String taskDescription,
                                                     @RequestParam(required = false) LocalDate deadline,
                                                     @RequestParam(required = false) String categoryName,
                                                     @RequestParam(defaultValue = "0") @Min(0) int pageNumber,
                                                     @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        val input = new SearchTask(
            userName, taskName, taskDescription, deadline, categoryName,
            new CernPageable(pageNumber, pageSize)
        );

        val result = taskService.getTasks(input);

        val apiResult = pageMapper.businessToApi(result, taskMapper::businessToApi);

        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/v1/task")
    public ResponseEntity<TaskApi> createTask(@Validated @RequestBody CreateTaskApi body) {
        val loggedUserInfo = LoggedUserUtils.getLoggedUserInfo();

        val input = Task.builder()
            .taskName(body.getTaskName())
            .taskDescription(body.getTaskDescription())
            .deadline(body.getDeadline())
            .categoryId(body.getCategoryId())
            .build();

        val result = taskService.createTask(input, loggedUserInfo);

        val apiResult = taskMapper.businessToApi(result);

        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

    @PutMapping("/v1/task/{taskId}")
    public ResponseEntity<TaskApi> updateTask(@PathVariable Long taskId,
                                              @Validated @RequestBody UpdateTaskApi body) {
        val loggedUserInfo = LoggedUserUtils.getLoggedUserInfo();

        val input = Task.builder()
            .taskId(taskId)
            .taskName(body.getTaskName())
            .taskDescription(body.getTaskDescription())
            .deadline(body.getDeadline())
            .categoryId(body.getCategoryId())
            .build();

        val result = taskService.updateTask(input, loggedUserInfo);

        val apiResult = taskMapper.businessToApi(result);

        return ResponseEntity.ok(apiResult);
    }

    @DeleteMapping("/v1/task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);

        return ResponseEntity.noContent().build();
    }
}
