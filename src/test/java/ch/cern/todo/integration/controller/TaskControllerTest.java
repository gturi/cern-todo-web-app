package ch.cern.todo.integration.controller;

import ch.cern.todo.controller.TaskController;
import ch.cern.todo.integration.config.ContainerTest;
import ch.cern.todo.integration.config.SpringBootIntegrationTest;
import ch.cern.todo.integration.util.HttpHeadersUtil;
import ch.cern.todo.integration.util.ObjectMapperUtil;
import ch.cern.todo.model.api.CreateTaskApi;
import ch.cern.todo.model.api.UpdateTaskApi;
import ch.cern.todo.model.business.LoggedUserInfo;
import ch.cern.todo.model.business.Role;
import ch.cern.todo.model.business.SearchTask;
import ch.cern.todo.model.business.Task;
import ch.cern.todo.model.mapper.PageMapper;
import ch.cern.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootIntegrationTest
@ContainerTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private PageMapper pageMapper;

    @MockitoSpyBean
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void getTask_ShouldReturnTask() throws Exception {
        mockMvc.perform(get("/v1/task/1").headers(HttpHeadersUtil.getAdminHeaders()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.taskId").value(1))
            .andExpect(jsonPath("$.taskName").value("Task 1"))
            .andExpect(jsonPath("$.taskDescription").value("Task 1 description"))
            .andExpect(jsonPath("$.deadline").value("2025-01-25T20:41:23"))
            .andExpect(jsonPath("$.categoryId").value(1))
            .andExpect(jsonPath("$.taskCategory.categoryId").value(1))
            .andExpect(jsonPath("$.taskCategory.categoryName").value("Category 1"))
            .andExpect(jsonPath("$.taskCategory.categoryDescription").value("Category 1 description"))
            .andExpect(jsonPath("$.assignedToUserInfo.userId").value(1))
            .andExpect(jsonPath("$.assignedToUserInfo.username").value("cernAdmin"));

        verify(taskService, times(1)).getTask(1L, adminLoggedUserInfo());
    }

    @Test
    void createTask_ShouldCreateAndReturnTask() throws Exception {
        CreateTaskApi createTaskApi = new CreateTaskApi();
        createTaskApi.setTaskName("New Task");
        createTaskApi.setTaskDescription("New Description");
        createTaskApi.setDeadline(LocalDate.of(2025, 2, 7).atStartOfDay());
        createTaskApi.setCategoryId(2L);
        createTaskApi.setUserId(3L);

        mockMvc.perform(post("/v1/task")
                .headers(HttpHeadersUtil.getAdminHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtil.asJsonString(createTaskApi)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.taskId").isNumber())
            .andExpect(jsonPath("$.taskName").value("New Task"))
            .andExpect(jsonPath("$.taskDescription").value("New Description"))
            .andExpect(jsonPath("$.deadline").value("2025-02-07T00:00:00"))
            .andExpect(jsonPath("$.categoryId").value(2))
            .andExpect(jsonPath("$.taskCategory.categoryId").value(2))
            .andExpect(jsonPath("$.taskCategory.categoryName").value("Category 2"))
            .andExpect(jsonPath("$.taskCategory.categoryDescription").value("Category 2 description"))
            .andExpect(jsonPath("$.assignedToUserInfo.userId").value(3))
            .andExpect(jsonPath("$.assignedToUserInfo.username").value("bobUser"));

        verify(taskService, times(1)).createTask(any(Task.class), eq(adminLoggedUserInfo()));
    }

    @Test
    void updateTask_ShouldUpdateAndReturnTask() throws Exception {
        UpdateTaskApi updateTaskApi = new UpdateTaskApi();
        updateTaskApi.setTaskName("Updated Task");
        updateTaskApi.setTaskDescription("Updated Description");
        updateTaskApi.setDeadline(LocalDate.of(2025, 2, 12).atStartOfDay());
        updateTaskApi.setCategoryId(3L);
        updateTaskApi.setUserId(4L);

        mockMvc.perform(put("/v1/task/1")
                .headers(HttpHeadersUtil.getAdminHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtil.asJsonString(updateTaskApi)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.taskId").value(1))
            .andExpect(jsonPath("$.taskName").value("Updated Task"))
            .andExpect(jsonPath("$.taskDescription").value("Updated Description"))
            .andExpect(jsonPath("$.deadline").value("2025-02-12T00:00:00"))
            .andExpect(jsonPath("$.categoryId").value(3))
            .andExpect(jsonPath("$.taskCategory.categoryId").value(3))
            .andExpect(jsonPath("$.taskCategory.categoryName").value("Category 3"))
            .andExpect(jsonPath("$.taskCategory.categoryDescription").value("Category 3 description"))
            .andExpect(jsonPath("$.assignedToUserInfo.userId").value(4))
            .andExpect(jsonPath("$.assignedToUserInfo.username").value("chrisUser"));

        verify(taskService, times(1)).updateTask(any(Task.class), eq(adminLoggedUserInfo()));
    }

    @Test
    void deleteTask_ShouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/v1/task/1").headers(HttpHeadersUtil.getAdminHeaders()))
            .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L, adminLoggedUserInfo());
    }

    @Test
    void getTasks_AsAdmin_ShouldReturnTasksAssignedToAnyUser() throws Exception {
        mockMvc.perform(get("/v1/task")
                .headers(HttpHeadersUtil.getAdminHeaders()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(10))
            .andExpect(jsonPath("$.content[0].assignedToUserInfo.userId").value(1))
            .andExpect(jsonPath("$.content[1].assignedToUserInfo.userId").value(1))
            .andExpect(jsonPath("$.content[2].assignedToUserInfo.userId").value(1))
            .andExpect(jsonPath("$.content[3].assignedToUserInfo.userId").value(1))
            .andExpect(jsonPath("$.content[4].assignedToUserInfo.userId").value(2))
            .andExpect(jsonPath("$.content[5].assignedToUserInfo.userId").value(2))
            .andExpect(jsonPath("$.content[6].assignedToUserInfo.userId").value(1))
            .andExpect(jsonPath("$.content[7].assignedToUserInfo.userId").value(3))
            .andExpect(jsonPath("$.content[8].assignedToUserInfo.userId").value(3))
            .andExpect(jsonPath("$.content[9].assignedToUserInfo.userId").value(3));

        verify(taskService, times(1)).getTasks(any(SearchTask.class), eq(adminLoggedUserInfo()));
        verify(pageMapper, times(1)).businessToApi(any(), any());
    }

    @Test
    void getTasks_AsUser_ShouldReturnTasksAssignedOnlyToThatUser() throws Exception {
        mockMvc.perform(get("/v1/task")
                .param("username", "bobUser") // this should get ignored
                .headers(HttpHeadersUtil.getAliceHeaders()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].assignedToUserInfo.userId").value(2))
            .andExpect(jsonPath("$.content[1].assignedToUserInfo.userId").value(2));

        verify(taskService, times(1)).getTasks(any(SearchTask.class), eq(aliceLoggedUserInfo()));
        verify(pageMapper, times(1)).businessToApi(any(), any());
    }

    private LoggedUserInfo adminLoggedUserInfo() {
        return new LoggedUserInfo(
            1L, "cernAdmin", "Cern", "Admin", Set.of(Role.ROLE_ADMIN)
        );
    }

    private LoggedUserInfo aliceLoggedUserInfo() {
        return new LoggedUserInfo(
            2L, "aliceUser", "Alice", "Ikari", Set.of(Role.ROLE_USER)
        );
    }
}
