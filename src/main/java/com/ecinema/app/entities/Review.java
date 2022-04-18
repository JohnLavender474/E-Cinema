package com.ecinema.app.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
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

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

}
