package com.ecinema.app.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class AdminTraineeAuthority extends AbstractEntity {

    @Column
    private Integer percentageTrainingModulesCompleted;

    @ManyToOne
    @JoinColumn
    private AdminAuthority mentor;

}
