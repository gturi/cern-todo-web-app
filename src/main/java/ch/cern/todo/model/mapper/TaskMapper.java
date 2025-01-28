package ch.cern.todo.model.mapper;

import ch.cern.todo.model.api.TaskApi;
import ch.cern.todo.model.business.Task;
import ch.cern.todo.model.database.TaskEntity;
import jakarta.persistence.Column;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskApi businessToApi(Task task);

    @Mapping(target = "updateUserId", ignore = true)
    @Mapping(target = "creationUserId", ignore = true)
    @Mapping(target = "assignedToUsername", ignore = true)
    @Mapping(target = "assignedToUserId", ignore = true)
    TaskEntity businessToEntity(Task task);

    @Mapping(target = "assignedToUserInfo.userId", source = "assignedToUserId")
    @Mapping(target = "assignedToUserInfo.username", source = "assignedToUsername")
    Task entityToBusiness(TaskEntity taskEntity);
}
