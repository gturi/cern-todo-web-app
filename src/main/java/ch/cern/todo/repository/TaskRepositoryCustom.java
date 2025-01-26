package ch.cern.todo.repository;

import ch.cern.todo.model.business.CernPage;
import ch.cern.todo.model.business.LoggedUserInfo;
import ch.cern.todo.model.business.SearchTask;
import ch.cern.todo.model.database.TaskEntity;

public interface TaskRepositoryCustom {

    int countTasks(SearchTask searchTask, LoggedUserInfo loggedUserInfo);

    CernPage<TaskEntity> findTasks(SearchTask searchTask,  LoggedUserInfo loggedUserInfo);
}
