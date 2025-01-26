package ch.cern.todo.model.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity extends AbstractEntity {

    private static final String SEQUENCE_GENERATOR_NAME = "tasks_generator_name";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR_NAME)
    @SequenceGenerator(name = SEQUENCE_GENERATOR_NAME, sequenceName = "tasks_task_id_seq", allocationSize = 1)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "creation_user_id", updatable = false)
    private Long creationUserId;

    @Column(name = "creation_username", updatable = false)
    private String creationUsername;

    @Column(name = "update_user_id")
    private Long updateUserId;

    @Column(name = "update_username")
    private String updateUsername;

    @Column(name = "task_name", nullable = false)
    private String taskName;

    @Column(name = "task_description")
    private String taskDescription;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false)
    private TaskCategoryEntity taskCategory;
}
