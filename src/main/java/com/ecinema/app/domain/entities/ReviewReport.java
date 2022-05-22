package com.ecinema.app.domain.entities;

import com.ecinema.app.domain.enums.Report;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ReviewReport extends AbstractEntity {

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer reporter;

    @Column
    @Enumerated(EnumType.STRING)
    private Report report;

}
