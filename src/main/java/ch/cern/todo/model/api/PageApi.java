package ch.cern.todo.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageImpl;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageApi<T extends Serializable> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1301766106610597865L;

    private List<T> content;
    /**
     * {@link PageImpl#getNumber()}
     */
    private int number;
    /**
     * {@link PageImpl#getSize()}
     */
    private int size;
    /**
     * {@link PageImpl#getNumberOfElements()}
     */
    private int numberOfElements;
    /**
     * {@link PageImpl#getTotalPages()}
     */
    private long totalPages;
}
