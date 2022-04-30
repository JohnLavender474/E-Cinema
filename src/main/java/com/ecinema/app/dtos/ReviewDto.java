package com.ecinema.app.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDto implements Serializable {
    private Long id;
    private String review;
    private Integer likes;
    private Integer dislikes;
    private Boolean isCensored;
    private LocalDateTime creationDateTime;
}
