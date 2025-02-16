package ch.cern.todo.repository;

import ch.cern.todo.model.business.CernPage;
import ch.cern.todo.model.business.LoggedUserInfo;
import ch.cern.todo.model.business.SearchTask;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public long countTasks(SearchTask searchTask, LoggedUserInfo loggedUserInfo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<TaskEntity> task = criteriaQuery.from(TaskEntity.class);

        List<Predicate> predicates = buildFindTasksPredicates(criteriaBuilder, task, searchTask, loggedUserInfo);

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        criteriaQuery.select(criteriaBuilder.count(task));

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getSingleResult();
    }

    @Transactional
    @Override
    public CernPage<TaskEntity> findTasks(SearchTask searchTask, LoggedUserInfo loggedUserInfo) {
        val count = countTasks(searchTask, loggedUserInfo);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskEntity> criteriaQuery = criteriaBuilder.createQuery(TaskEntity.class);

        Root<TaskEntity> task = criteriaQuery.from(TaskEntity.class);
        task.fetch(TaskEntity_.TASK_CATEGORY, JoinType.INNER);

        val selectQueryPredicates = buildFindTasksPredicates(criteriaBuilder, task, searchTask, loggedUserInfo);

        val selectQuery = criteriaQuery.where(selectQueryPredicates.toArray(new Predicate[0]))
            .orderBy(criteriaBuilder.asc(task.get(TaskEntity_.taskId)));

        TypedQuery<TaskEntity> typedQuery = entityManager.createQuery(selectQuery);
        typedQuery.setFirstResult(searchTask.getPageable().getPageNumber() * searchTask.getPageable().getPageSize());
        typedQuery.setMaxResults(searchTask.getPageable().getPageSize());

        List<TaskEntity> taskEntities = typedQuery.getResultList();

        return new CernPage<>(taskEntities, searchTask.getPageable(), count);
    }

    private List<Predicate> buildFindTasksPredicates(CriteriaBuilder criteriaBuilder, Root<TaskEntity> task,
                                                     SearchTask searchTask, LoggedUserInfo loggedUserInfo) {
        List<Predicate> predicates = new ArrayList<>();

        if (!loggedUserInfo.isAdmin()) {
            // not admin users can only see the tasks assigned to them
            predicates.add(criteriaBuilder.equal(task.get(TaskEntity_.assignedToUserId), loggedUserInfo.userId()));
        }
        // if the current user is not an admin, it does not make sense to add this filter,
        // since he can see only the tasks assigned to him
        if (loggedUserInfo.isAdmin() && StringUtils.isNotBlank(searchTask.getUserName())) {
            predicates.add(criteriaBuilder.equal(task.get(TaskEntity_.assignedToUsername), searchTask.getUserName()));
        }
        if (StringUtils.isNotBlank(searchTask.getTaskName())) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(task.get(TaskEntity_.taskName)),
                "%" + searchTask.getTaskName().toLowerCase() + "%"
            ));
        }
        if (StringUtils.isNotBlank(searchTask.getTaskDescription())) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(task.get(TaskEntity_.taskDescription)),
                "%" + searchTask.getTaskDescription().toLowerCase() + "%"
            ));
        }
        if (searchTask.getDeadline() != null) {
            LocalDateTime startOfDay = searchTask.getDeadline().atStartOfDay();
            LocalDateTime endOfDay = searchTask.getDeadline().atTime(23, 59, 59);
            predicates.add(criteriaBuilder.between(task.get(TaskEntity_.deadline), startOfDay, endOfDay));
        }
        if (StringUtils.isNotBlank(searchTask.getCategoryName())) {
            predicates.add(criteriaBuilder.equal(
                criteriaBuilder.lower(task.get(TaskEntity_.TASK_CATEGORY).get(TaskCategoryEntity_.CATEGORY_NAME)),
                searchTask.getCategoryName().toLowerCase()
            ));
        }

        return predicates;
    }
}
