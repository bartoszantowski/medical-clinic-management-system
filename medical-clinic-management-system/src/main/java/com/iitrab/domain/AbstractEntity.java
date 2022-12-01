package com.iitrab.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @Version
    private Long version;

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
        this.updateDate = createDate;
    }

    @PreUpdate
    public void updateDate() {
        this.updateDate = LocalDateTime.now();
    }
}
