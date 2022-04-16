package com.ecinema.app.review;

import com.ecinema.app.abstraction.AbstractEntity;
import com.ecinema.app.security.AppUser;
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
    private LocalDateTime creationDateAndTime;

    @OneToOne(fetch = FetchType.LAZY)
    private AppUser appUser;

}
