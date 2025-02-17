package ch.cern.todo.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskApi implements Serializable {
    @Serial
    private static final long serialVersionUID = 5107724138527177091L;

    private String taskName;
    private String taskDescription;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime deadline;
    private Long categoryId;
    private Long userId;
}
