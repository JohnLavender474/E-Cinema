package com.ecinema.app.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Review extends AbstractEntity {

    @Column
    private String review;

    @Column
    private Integer likes;

    @Column
    private Integer dislikes;

    @Column
    private LocalDateTime creationDateTime;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private CustomerRoleDef customerRoleDef;

}
