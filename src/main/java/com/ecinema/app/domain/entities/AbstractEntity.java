package com.ecinema.app.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * The id of this entity is an auto-generated Long value.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
