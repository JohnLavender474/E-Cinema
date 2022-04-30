package com.ecinema.app.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * {@inheritDoc}
 * This class represents a review that a user has written and posted for a particular {@link Movie}.
 * This class is mapped to {@link #writer} which owns this instance. {@link User} instances
 * with a customer role def are allowed to write and post reviews and post a single like or dislike
 * for any other review instance. A review can optionally be censored by a {@link ModeratorRoleDef}
 * which means that the review has been removed from public viewing until the moderator unlocks it.
 */
@Getter
@Setter
@Entity
public class Review extends AbstractEntity {

    @Column(length = 10000)
    private String review;

    @Column
    private Integer likes;

    @Column
    private Integer dislikes;

    @Column
    private Boolean isCensored;

    @Column
    private LocalDateTime creationDateTime;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerRoleDef writer;

}
