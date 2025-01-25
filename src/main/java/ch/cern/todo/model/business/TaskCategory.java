package ch.cern.todo.model.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCategory {
    private Long categoryId;
    private String categoryName;
    private String categoryDescription;
}
