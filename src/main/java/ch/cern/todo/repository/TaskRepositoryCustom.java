package ch.cern.todo.repository;

import ch.cern.todo.model.business.CernPageable;
import ch.cern.todo.model.database.TaskEntity;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface TaskRepositoryCustom {

    Page<TaskEntity> findTasks(String userName, String taskName, String taskDescription, LocalDate deadline,
                               String categoryName, CernPageable pageable);
}
