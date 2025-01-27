package ch.cern.todo.model.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateTaskApi implements Serializable {
    @Serial
    private static final long serialVersionUID = -7265639674428589738L;

    @NotBlank
    private String taskName;
    private String taskDescription;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime deadline;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long userId;
}
