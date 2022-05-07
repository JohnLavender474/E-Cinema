package com.ecinema.app.domain.forms;

import com.ecinema.app.domain.contracts.IReview;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReviewForm implements IReview, Serializable {
    private Long userId;
    private Long movieId;
    private String review;
    private Integer rating;
}
