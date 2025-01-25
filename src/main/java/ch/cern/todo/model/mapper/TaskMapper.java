package ch.cern.todo.model.mapper;

import ch.cern.todo.model.api.TaskApi;
import ch.cern.todo.model.business.Task;
import ch.cern.todo.model.database.TaskEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskApi businessToApi(Task task);

    TaskEntity businessToEntity(Task task);

    Task entityToBusiness(TaskEntity taskEntity);
}
