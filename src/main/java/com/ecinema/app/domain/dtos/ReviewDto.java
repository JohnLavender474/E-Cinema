package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.IReview;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDto implements AbstractDto, IReview {
    private Long id = 0L;
    private Long customerId = 0L;
    private String writer = "";
    private String review = "";
    private Integer rating = 0;
    private Boolean isCensored = false;
    private LocalDateTime creationDateTime;
}
