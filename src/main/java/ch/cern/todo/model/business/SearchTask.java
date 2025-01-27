package ch.cern.todo.model.business;

import jakarta.annotation.Nullable;
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
    @Nullable
    private String userName;
    @Nullable
    private String taskName;
    @Nullable
    private String taskDescription;
    @Nullable
    private LocalDate deadline;
    @Nullable
    private String categoryName;
    private CernPageable pageable;
}
