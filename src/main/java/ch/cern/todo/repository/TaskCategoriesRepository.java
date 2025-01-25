package ch.cern.todo.repository;

import ch.cern.todo.model.database.TaskCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCategoriesRepository extends JpaRepository<TaskCategoryEntity, Long> {
}
