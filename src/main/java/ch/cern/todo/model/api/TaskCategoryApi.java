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
public class TaskCategoryApi implements Serializable {
    @Serial
    private static final long serialVersionUID = -5266041275861229011L;

    private Long categoryId;
    private String categoryName;
    private String categoryDescription;
}
