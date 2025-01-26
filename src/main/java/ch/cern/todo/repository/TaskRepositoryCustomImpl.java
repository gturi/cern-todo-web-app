package ch.cern.todo.repository;

import ch.cern.todo.model.business.CernPageable;
import ch.cern.todo.model.database.TaskCategoryEntity_;
import ch.cern.todo.model.database.TaskEntity;
import ch.cern.todo.model.database.TaskEntity_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Page<TaskEntity> findTasks(String userName, String taskName, String taskDescription, LocalDate deadline,
                                      String categoryName, CernPageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskEntity> criteriaQuery = criteriaBuilder.createQuery(TaskEntity.class);

        Root<TaskEntity> task = criteriaQuery.from(TaskEntity.class);
        task.fetch(TaskEntity_.TASK_CATEGORY, JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(userName)) { // TODO: skip this if not admin
            //predicates.add(criteriaBuilder.equals(task.get(TaskEntity_.), "%" + userName.toLowerCase() + "%"));
        }
        if (StringUtils.isNotBlank(taskName)) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(task.get(TaskEntity_.taskName)),
                "%" + taskName.toLowerCase() + "%"
            ));
        }
        if (StringUtils.isNotBlank(taskDescription)) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(task.get(TaskEntity_.taskDescription)),
                "%" + taskDescription.toLowerCase() + "%"
            ));
        }
        if (deadline != null) {
            LocalDateTime startOfDay = deadline.atStartOfDay();
            LocalDateTime endOfDay = deadline.atTime(23, 59, 59);
            predicates.add(criteriaBuilder.between(task.get(TaskEntity_.deadline), startOfDay, endOfDay));
        }
        if (StringUtils.isNotBlank(categoryName)) {
            predicates.add(criteriaBuilder.equal(
                criteriaBuilder.lower(task.get(TaskEntity_.TASK_CATEGORY).get(TaskCategoryEntity_.CATEGORY_NAME)),
                categoryName.toLowerCase()
            ));
        }

        val selectQuery = criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<TaskEntity> typedQuery = entityManager.createQuery(selectQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<TaskEntity> taskEntities = typedQuery.getResultList();
        return new PageImpl<>(taskEntities);
    }
}
