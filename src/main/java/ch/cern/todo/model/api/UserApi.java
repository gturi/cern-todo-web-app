package ch.cern.todo.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserApi implements Serializable {
    @Serial
    private static final long serialVersionUID = -3143918791323490171L;

    private Long userId;
    private String username;
}
