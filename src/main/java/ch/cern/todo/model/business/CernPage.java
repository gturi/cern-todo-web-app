package ch.cern.todo.model.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.function.Function;

@Data
@AllArgsConstructor
public class CernPage<T> {

    private List<T> content;
    /**
     * {@link PageImpl#getNumber()}
     */
    private final int number;
    /**
     * {@link PageImpl#getSize()}
     */
    private final int size;
    /**
     * {@link PageImpl#getNumberOfElements()}
     */
    private final int numberOfElements;
    /**
     * {@link PageImpl#getTotalPages()}
     */
    private final int totalPages;

    public <K> CernPage<K> map(Function<T, K> mapper) {
        return new CernPage<>(content.stream().map(mapper).toList(), number, size, numberOfElements, totalPages);
    }
}
