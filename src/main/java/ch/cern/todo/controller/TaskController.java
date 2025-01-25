package ch.cern.todo.controller;

import ch.cern.todo.model.api.CreateTaskApi;
import ch.cern.todo.model.api.TaskApi;
import ch.cern.todo.model.business.Task;
import ch.cern.todo.model.mapper.TaskMapper;
import ch.cern.todo.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @PostMapping("/task")
    public ResponseEntity<TaskApi> createTask(@Validated @RequestBody CreateTaskApi body) {
        val input = Task.builder()
            .taskName(body.getTaskName())
            .taskDescription(body.getTaskDescription())
            .deadline(body.getDeadline())
            .categoryId(body.getCategoryId())
            .build();

        val result = taskService.createTask(input);

        val apiResult = taskMapper.businessToApi(result);

        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }
}
