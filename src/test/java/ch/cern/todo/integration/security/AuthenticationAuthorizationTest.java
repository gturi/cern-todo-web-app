package ch.cern.todo.integration.security;

import ch.cern.todo.controller.TaskController;
import ch.cern.todo.integration.config.ContainerTest;
import ch.cern.todo.integration.config.SpringBootIntegrationTest;
import ch.cern.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static ch.cern.todo.integration.util.HttpHeadersUtil.getAdminHeaders;
import static ch.cern.todo.integration.util.HttpHeadersUtil.getAliceHeaders;
import static ch.cern.todo.integration.util.HttpHeadersUtil.getAuthorizationHeaders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootIntegrationTest
@ContainerTest
@AutoConfigureMockMvc
class AuthenticationAuthorizationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private TaskService taskService;

    @Autowired
    private TaskController taskController;

    @Test
    void getApi_WithoutCredentials_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/v1/task"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.description").value("Unauthorized"));

        verify(taskService, times(0)).getTasks(any(), any());
    }

    @Test
    void getApi_WithoutWrongCredentials_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/v1/task").headers(getAuthorizationHeaders("cernAdmin", "wrong-password")))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.description").value("Unauthorized"));

        verify(taskService, times(0)).getTasks(any(), any());
    }

    @Test
    void getApi_WithUserCredentials_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/v1/task").headers(getAliceHeaders()))
            .andExpect(status().isOk());

        verify(taskService, times(1)).getTasks(any(), any());
    }

    @Test
    void getApi_WithAdminCredentials_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/v1/task").headers(getAdminHeaders()))
            .andExpect(status().isOk());

        verify(taskService, times(1)).getTasks(any(), any());
    }

    @Test
    void getAdminApi_WithAdminCredentials_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/v1/admin/task").headers(getAdminHeaders()))
            .andExpect(status().isOk());

        verify(taskService, times(1)).getTasks(any(), any());
    }

    @Test
    void getAdminApi_WithUserCredentials_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/v1/admin/task").headers(getAliceHeaders()))
            .andExpect(status().isForbidden());

        verify(taskService, times(0)).getTasks(any(), any());
    }

    @Test
    void getUserApi_WithUserCredentials_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/v1/user/task").headers(getAliceHeaders()))
            .andExpect(status().isOk());

        verify(taskService, times(1)).getTasks(any(), any());
    }

    @Test
    void getUserApi_WithAdminCredentials_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/v1/user/task").headers(getAdminHeaders()))
            .andExpect(status().isForbidden());

        verify(taskService, times(0)).getTasks(any(), any());
    }
}
