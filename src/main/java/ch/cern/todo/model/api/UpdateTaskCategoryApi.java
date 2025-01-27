package ch.cern.todo.model.api;

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
public class UpdateTaskCategoryApi implements Serializable {
    @Serial
    private static final long serialVersionUID = 3998541611804801666L;

    private String categoryName;
    private String categoryDescription;
}
