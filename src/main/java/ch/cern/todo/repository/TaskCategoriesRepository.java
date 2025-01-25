package ch.cern.todo.repository;

import ch.cern.todo.model.database.TaskCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCategoriesRepository extends JpaRepository<TaskCategoryEntity, Long> {
}
