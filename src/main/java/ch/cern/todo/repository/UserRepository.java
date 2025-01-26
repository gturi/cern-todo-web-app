package ch.cern.todo.repository;

import ch.cern.todo.model.database.UserEntity;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Nullable
    UserEntity findByUsername(String username);
}
