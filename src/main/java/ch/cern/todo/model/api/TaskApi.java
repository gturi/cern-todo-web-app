package ch.cern.todo.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskApi implements Serializable {
    @Serial
    private static final long serialVersionUID = 5673537870478297771L;

    private Long taskId;
    private String taskName;
    private String taskDescription;
    private LocalDateTime deadline;
    private Long categoryId;
    private TaskCategoryApi taskCategory;
    private UserApi assignedToUserInfo;
}
