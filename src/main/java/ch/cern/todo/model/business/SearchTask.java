package ch.cern.todo.model.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchTask {
    private String userName;
    private String taskName;
    private String taskDescription;
    private LocalDate deadline;
    private String categoryName;
    private CernPageable pageable;
}
