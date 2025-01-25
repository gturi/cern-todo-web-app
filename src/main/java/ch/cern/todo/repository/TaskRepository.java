package ch.cern.todo.repository;

import ch.cern.todo.model.database.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query("""
        select t
        from TaskEntity t
        join fetch t.taskCategory
        where t.taskId = :taskId
        """)
    TaskEntity getTaskById(@Param("taskId") Long taskId);
}
