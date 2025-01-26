package ch.cern.todo.model.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUserInfo {
    private Long userId;
    private String username;
}
