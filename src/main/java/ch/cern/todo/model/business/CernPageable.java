package ch.cern.todo.model.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
public class CernPageable {

    /**
     * {@link Pageable#getPageNumber()}
     */
    private final int pageNumber;
    /**
     * {@link Pageable#getPageSize()}
     */
    private final int pageSize;
}
