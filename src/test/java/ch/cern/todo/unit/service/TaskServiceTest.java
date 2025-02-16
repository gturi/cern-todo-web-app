package ch.cern.todo.unit.service;

import ch.cern.todo.model.business.CernException;
import ch.cern.todo.model.business.LoggedUserInfo;
import ch.cern.todo.model.business.Role;
import ch.cern.todo.model.business.Task;
import ch.cern.todo.model.business.TaskUserInfo;
import ch.cern.todo.model.database.TaskCategoryEntity;
import ch.cern.todo.model.database.TaskEntity;
import ch.cern.todo.model.database.UserEntity;
import ch.cern.todo.model.mapper.TaskMapper;
import ch.cern.todo.repository.TaskCategoriesRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.repository.UserRepository;
import ch.cern.todo.service.TaskService;
import lombok.val;
import org.apache.commons.collections4.SetUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskCategoriesRepository taskCategoriesRepository;

    @Mock
    private UserRepository userRepository;

    private TaskService taskService;

    private LoggedUserInfo adminUser;
    private LoggedUserInfo regularUser;

    @BeforeEach
    void setUp() {
        adminUser = new LoggedUserInfo(
            1L, "cernAdmin", "Cern", "Admin", SetUtils.hashSet(Role.ROLE_ADMIN)
        );
        regularUser = new LoggedUserInfo(
            2L, "aliceUser", "Alice", "Ikari", SetUtils.hashSet(Role.ROLE_USER)
        );

        TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

        taskService = new TaskService(taskMapper, taskRepository, taskCategoriesRepository, userRepository);
    }

    @Test
    void getTask_TaskExistsAndAuthorized_ShouldReturnTask() {
        val taskEntity = new TaskEntity();
        taskEntity.setAssignedToUserId(2L);

        when(taskRepository.getTaskById(1L)).thenReturn(taskEntity);

        Task result = taskService.getTask(1L, regularUser);

        assertNotNull(result);
        verify(taskRepository, times(1)).getTaskById(1L);
    }

    @Test
    void getTask_TaskNotFound_ShouldThrowException() {
        when(taskRepository.getTaskById(1L)).thenReturn(null);

        CernException exception = assertThrows(CernException.class, () -> taskService.getTask(1L, regularUser));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        verify(taskRepository, times(1)).getTaskById(1L);
    }

    @Test
    void createTask_ValidTask_ShouldReturnCreatedTask() {
        val taskCategoryEntity = new TaskCategoryEntity();
        val userEntity = new UserEntity();
        userEntity.setUserId(2L);
        userEntity.setUsername("aliceUser");

        when(taskCategoriesRepository.findById(1L)).thenReturn(Optional.of(taskCategoryEntity));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userEntity));

        val task = new Task();
        task.setTaskId(1L);
        task.setCategoryId(1L);
        task.setAssignedToUserInfo(TaskUserInfo.builder()
            .userId(regularUser.userId())
            .build());

        Task result = taskService.createTask(task, adminUser);

        assertNotNull(result);
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void createTask_MissingAssignedUser_ShouldThrowException() {
        val task = new Task();
        task.setCategoryId(1L);
        task.setAssignedToUserInfo(new TaskUserInfo());

        CernException exception = assertThrows(CernException.class, () -> taskService.createTask(task, adminUser));

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals("Assigned to user is required", exception.getMessage());
    }

    @Test
    void updateTask_TaskExistsAndAuthorized_ShouldUpdateTask() {
        val taskEntity = new TaskEntity();
        taskEntity.setTaskId(1L);
        taskEntity.setCreationUserId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskCategoriesRepository.findById(2L)).thenReturn(Optional.of(TaskCategoryEntity.builder()
            .categoryId(2L)
            .build()));

        val task = new Task();
        task.setTaskId(1L);
        task.setCategoryId(2L);
        task.setAssignedToUserInfo(new TaskUserInfo());

        Task result = taskService.updateTask(task, adminUser);

        assertNotNull(result);
        verify(taskRepository, times(1)).save(taskEntity);
    }

    @Test
    void updateTask_UnauthorizedUser_ShouldThrowException() {
        val taskEntity = new TaskEntity();
        taskEntity.setTaskId(1L);
        taskEntity.setCreationUserId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        val task = new Task();
        task.setTaskId(1L);
        task.setCategoryId(2L);

        CernException exception = assertThrows(CernException.class, () -> taskService.updateTask(task, regularUser));

        assertEquals(HttpStatus.UNAUTHORIZED.value(), exception.getStatusCode());
    }

    @Test
    void deleteTask_TaskExistsAndAuthorized_ShouldDeleteTask() {
        when(taskRepository.getCreatorUserId(1L)).thenReturn(1L);

        taskService.deleteTask(1L, adminUser);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_UnauthorizedUser_ShouldThrowException() {
        when(taskRepository.getCreatorUserId(1L)).thenReturn(1L);

        CernException exception = assertThrows(CernException.class, () ->
            taskService.deleteTask(1L, regularUser)
        );

        assertEquals(HttpStatus.UNAUTHORIZED.value(), exception.getStatusCode());
    }
}
