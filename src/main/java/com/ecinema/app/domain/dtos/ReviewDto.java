package com.ecinema.app.domain.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDto implements AbstractDto {
    private Long id;
    private String writer;
    private String review;
    private Integer rating;
    private Boolean isCensored;
    private LocalDateTime creationDateTime;
}
