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

    TaskEntity businessToEntity(Task task);

    @Mapping(target = "lastUpdateUserInfo.userId", source = "updateUserId")
    @Mapping(target = "lastUpdateUserInfo.firstname", source = "updateUserFirstname")
    @Mapping(target = "lastUpdateUserInfo.lastname", source = "updateUserLastname")
    Task entityToBusiness(TaskEntity taskEntity);
}
