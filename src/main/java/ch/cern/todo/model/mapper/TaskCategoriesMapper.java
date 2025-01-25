package ch.cern.todo.model.mapper;

import ch.cern.todo.model.api.TaskCategoryApi;
import ch.cern.todo.model.business.TaskCategory;
import ch.cern.todo.model.database.TaskCategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskCategoriesMapper {

    TaskCategoryApi businessToApi(TaskCategory taskCategory);

    TaskCategoryEntity businessToEntity(TaskCategory taskCategory);

    TaskCategory entityToBusiness(TaskCategoryEntity taskCategoryEntity);
}
