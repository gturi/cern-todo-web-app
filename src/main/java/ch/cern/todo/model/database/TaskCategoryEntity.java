package ch.cern.todo.model.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_categories")
public class TaskCategoryEntity extends AbstractEntity {

    private static final String SEQUENCE_GENERATOR_NAME = "task_categories_generator_name";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR_NAME)
    @SequenceGenerator(name = SEQUENCE_GENERATOR_NAME, sequenceName = "task_categories_category_id_seq")
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;
}
