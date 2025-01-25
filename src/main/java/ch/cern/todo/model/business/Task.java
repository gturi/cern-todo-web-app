package ch.cern.todo.model.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private Long taskId;
    private String taskName;
    private String taskDescription;
    private LocalDateTime deadline;
    private Long categoryId;
    private TaskCategory taskCategory;
}
