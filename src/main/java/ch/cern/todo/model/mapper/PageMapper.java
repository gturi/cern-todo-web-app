package ch.cern.todo.model.mapper;

import ch.cern.todo.model.api.PageApi;
import ch.cern.todo.model.business.CernPage;
import org.mapstruct.Mapper;

import java.io.Serializable;
import java.util.function.Function;

@Mapper(componentModel = "spring")
public interface PageMapper {

    default <T, K extends Serializable> PageApi<K> businessToApi(CernPage<T> cernPage, Function<T, K> mapper) {
        return new PageApi<>(
            cernPage.getContent().stream().map(mapper).toList(),
            cernPage.getNumber(),
            cernPage.getSize(),
            cernPage.getNumberOfElements(),
            cernPage.getTotalPages()
        );
    }
}
