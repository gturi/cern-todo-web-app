package ch.cern.todo.unit.service;

import ch.cern.todo.model.business.CernException;
import ch.cern.todo.model.business.TaskCategory;
import ch.cern.todo.model.database.TaskCategoryEntity;
import ch.cern.todo.model.mapper.TaskCategoriesMapper;
import ch.cern.todo.repository.TaskCategoriesRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.service.TaskCategoriesService;
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
class TaskCategoriesServiceTest {

    @Mock
    private TaskCategoriesRepository taskCategoriesRepository;
    @Mock
    private TaskRepository taskRepository;

    private TaskCategoriesService taskCategoriesService;

    private TaskCategory taskCategory;
    private TaskCategoryEntity taskCategoryEntity;

    @BeforeEach
    void setUp() {
        taskCategory = new TaskCategory();
        taskCategory.setCategoryName("Test Category");
        taskCategory.setCategoryDescription("Test Description");

        taskCategoryEntity = new TaskCategoryEntity();
        taskCategoryEntity.setCategoryId(1L);
        taskCategoryEntity.setCategoryName("Test Category");
        taskCategoryEntity.setCategoryDescription("Test Description");

        TaskCategoriesMapper taskCategoriesMapper = Mappers.getMapper(TaskCategoriesMapper.class);

        taskCategoriesService = new TaskCategoriesService(
            taskCategoriesMapper, taskCategoriesRepository, taskRepository
        );
    }

    @Test
    void getTaskCategory_ValidId_ShouldReturnTaskCategory() {
        when(taskCategoriesRepository.findById(1L)).thenReturn(Optional.of(taskCategoryEntity));

        TaskCategory result = taskCategoriesService.getTaskCategory(1L);

        assertNotNull(result);
        assertEquals("Test Category", result.getCategoryName());
        verify(taskCategoriesRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskCategory_InvalidId_ShouldThrowException() {
        when(taskCategoriesRepository.findById(1L)).thenReturn(Optional.empty());

        CernException exception = assertThrows(
            CernException.class, () -> taskCategoriesService.getTaskCategory(1L)
        );

        assertEquals("Task category not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        verify(taskCategoriesRepository, times(1)).findById(1L);
    }

    @Test
    void createTaskCategory_ValidCategory_ShouldReturnCreatedCategory() {
        when(taskCategoriesRepository.save(any())).thenReturn(taskCategoryEntity);

        TaskCategory result = taskCategoriesService.createTaskCategory(taskCategory);

        assertNotNull(result);
        assertEquals("Test Category", result.getCategoryName());
        verify(taskCategoriesRepository, times(1)).save(any());
    }

    @Test
    void updateTaskCategory_ValidCategory_ShouldReturnUpdatedCategory() {
        when(taskCategoriesRepository.findById(1L)).thenReturn(Optional.of(taskCategoryEntity));

        taskCategory.setCategoryId(1L);
        taskCategory.setCategoryName("Updated Category");
        taskCategory.setCategoryDescription("Updated Description");

        TaskCategory result = taskCategoriesService.updateTaskCategory(taskCategory);

        assertNotNull(result);
        assertEquals("Updated Category", result.getCategoryName());
        assertEquals("Updated Description", result.getCategoryDescription());
        verify(taskCategoriesRepository, times(1)).save(taskCategoryEntity);
    }

    @Test
    void updateTaskCategory_InvalidId_ShouldThrowException() {
        when(taskCategoriesRepository.findById(1L)).thenReturn(Optional.empty());

        taskCategory.setCategoryId(1L);

        CernException exception = assertThrows(CernException.class, () ->
            taskCategoriesService.updateTaskCategory(taskCategory)
        );

        assertEquals("Task category not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        verify(taskCategoriesRepository, times(1)).findById(1L);
    }

    @Test
    void deleteTaskCategory_ValidId_ShouldDeleteCategory() {
        when(taskCategoriesRepository.findById(1L)).thenReturn(Optional.of(taskCategoryEntity));
        when(taskRepository.existsTaskAssociatedToCategory(1L)).thenReturn(false);

        taskCategoriesService.deleteTaskCategory(1L);

        verify(taskCategoriesRepository, times(1)).delete(taskCategoryEntity);
        verify(taskRepository, times(1)).existsTaskAssociatedToCategory(1L);
    }

    @Test
    void deleteTaskCategory_InvalidId_ShouldThrowException() {
        when(taskCategoriesRepository.findById(1L)).thenReturn(Optional.empty());

        CernException exception = assertThrows(CernException.class, () ->
            taskCategoriesService.deleteTaskCategory(1L)
        );

        assertEquals("Task category not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        verify(taskCategoriesRepository, times(1)).findById(1L);
        verify(taskRepository, times(0)).existsTaskAssociatedToCategory(1L);
    }

    @Test
    void deleteTaskCategory_AssociatedToTask_ShouldThrowException() {
        when(taskCategoriesRepository.findById(1L)).thenReturn(Optional.of(taskCategoryEntity));
        when(taskRepository.existsTaskAssociatedToCategory(1L)).thenReturn(true);

        CernException exception = assertThrows(CernException.class, () ->
            taskCategoriesService.deleteTaskCategory(1L)
        );

        assertEquals("Cannot delete task category since it is associated to a task", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        verify(taskCategoriesRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).existsTaskAssociatedToCategory(1L);
    }
}
