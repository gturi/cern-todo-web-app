package ch.cern.todo.integration.controller;

import ch.cern.todo.controller.TaskCategoriesController;
import ch.cern.todo.integration.config.ContainerTest;
import ch.cern.todo.integration.config.SpringBootIntegrationTest;
import ch.cern.todo.integration.util.ObjectMapperUtil;
import ch.cern.todo.model.api.CreateTaskCategoryApi;
import ch.cern.todo.model.api.UpdateTaskCategoryApi;
import ch.cern.todo.model.business.TaskCategory;
import ch.cern.todo.model.mapper.TaskCategoriesMapper;
import ch.cern.todo.service.TaskCategoriesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static ch.cern.todo.integration.util.HttpHeadersUtil.getAdminHeaders;
import static org.mockito.Mockito.any;
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
class TaskCategoriesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private TaskCategoriesService taskCategoriesService;

    @MockitoSpyBean
    private TaskCategoriesMapper taskCategoriesMapper;

    @Autowired
    private TaskCategoriesController taskCategoriesController;

    @Test
    void getTaskCategory_ValidId_ShouldReturnCategory() throws Exception {
        mockMvc.perform(get("/v1/task-category/1").headers(getAdminHeaders()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryId").value(1))
            .andExpect(jsonPath("$.categoryName").value("Category 1"))
            .andExpect(jsonPath("$.categoryDescription").value("Category 1 description"));

        verify(taskCategoriesService, times(1)).getTaskCategory(1L);
        verify(taskCategoriesMapper, times(1)).businessToApi(any(TaskCategory.class));
    }

    @Test
    void createTaskCategory_ValidInput_ShouldCreateCategory() throws Exception {
        CreateTaskCategoryApi createTaskCategoryApi = new CreateTaskCategoryApi();
        createTaskCategoryApi.setCategoryName("New Category");
        createTaskCategoryApi.setCategoryDescription("New Description");

        mockMvc.perform(post("/v1/task-category")
                .headers(getAdminHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtil.asJsonString(createTaskCategoryApi)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.categoryId").isNotEmpty())
            .andExpect(jsonPath("$.categoryName").value("New Category"))
            .andExpect(jsonPath("$.categoryDescription").value("New Description"));

        verify(taskCategoriesService, times(1)).createTaskCategory(any(TaskCategory.class));
        verify(taskCategoriesMapper, times(1)).businessToApi(any(TaskCategory.class));
    }

    @Test
    void updateTaskCategory_ValidId_ShouldUpdateCategory() throws Exception {
        UpdateTaskCategoryApi updateTaskCategoryApi = new UpdateTaskCategoryApi();
        updateTaskCategoryApi.setCategoryName("Updated Category");
        updateTaskCategoryApi.setCategoryDescription("Updated Description");

        TaskCategory updatedTaskCategory = new TaskCategory();
        updatedTaskCategory.setCategoryId(1L);
        updatedTaskCategory.setCategoryName("Updated Category");
        updatedTaskCategory.setCategoryDescription("Updated Description");

        mockMvc.perform(put("/v1/task-category/1")
                .headers(getAdminHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtil.asJsonString(updatedTaskCategory)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryName").value("Updated Category"))
            .andExpect(jsonPath("$.categoryDescription").value("Updated Description"));

        verify(taskCategoriesService, times(1)).updateTaskCategory(any(TaskCategory.class));
        verify(taskCategoriesMapper, times(1)).businessToApi(any(TaskCategory.class));
    }

    @Test
    void deleteTaskCategory_ValidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/v1/task-category/3").headers(getAdminHeaders()))
            .andExpect(status().isNoContent());

        verify(taskCategoriesService, times(1)).deleteTaskCategory(3L);
    }
}
