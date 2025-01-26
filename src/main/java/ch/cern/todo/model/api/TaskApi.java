package ch.cern.todo.model.api;

import ch.cern.todo.model.business.TaskCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskApi implements Serializable {

    private Long taskId;
    private String taskName;
    private String taskDescription;
    private LocalDateTime deadline;
    private Long categoryId;
    private TaskCategoryApi taskCategory;
}
