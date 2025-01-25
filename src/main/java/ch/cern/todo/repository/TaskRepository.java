package ch.cern.todo.repository;

import ch.cern.todo.model.database.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
