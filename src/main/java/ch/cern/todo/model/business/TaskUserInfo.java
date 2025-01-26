package ch.cern.todo.model.business;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUserInfo {
    @Nullable
    private Long userId;
    @Nullable
    private String username;
}
