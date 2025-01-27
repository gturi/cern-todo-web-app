package ch.cern.todo.model.api;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskCategoryApi implements Serializable {
    @Serial
    private static final long serialVersionUID = -5294100124704128421L;

    @NotBlank
    private String categoryName;
    private String categoryDescription;
}
