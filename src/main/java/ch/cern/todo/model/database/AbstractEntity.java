package ch.cern.todo.model.database;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    protected LocalDateTime creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    protected LocalDateTime updateDate;

    @PrePersist
    public void onPersist() {
        creationDate = LocalDateTime.now();
        updateDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updateDate = LocalDateTime.now();
        ;
    }
}
