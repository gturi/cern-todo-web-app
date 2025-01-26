package ch.cern.todo.repository;

import ch.cern.todo.model.database.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, TaskRepositoryCustom {

    @Query("""
        select t
        from TaskEntity t
        join fetch t.taskCategory
        where t.taskName like %:taskName%
        """)
    Page<TaskEntity> getTaskByName(@Param("taskName") String taskName, Pageable pageable);

    @Query("""
        select t
        from TaskEntity t
        join fetch t.taskCategory
        where t.taskId = :taskId
        """)
    TaskEntity getTaskById(@Param("taskId") Long taskId);

    @Query("""
        select t.creationUserId
        from TaskEntity t
        where t.taskId = :taskId
        """)
    Long getCreatorUserId(@Param("taskId") Long taskId);
}
