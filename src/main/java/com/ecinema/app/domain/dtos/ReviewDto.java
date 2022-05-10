package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.IReview;
import com.ecinema.app.utils.UtilMethods;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.xml.SimplePropertyNamespaceHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class ReviewDto implements AbstractDto, IReview {

    private Long id = 0L;
    private Long customerId = 0L;
    private String writer = "";
    private String review = "";
    private Integer rating = 0;
    private Boolean isCensored = false;
    private LocalDateTime creationDateTime;

    public String creationDateTimeFormatted() {
        return UtilMethods.localDateTimeFormatted(creationDateTime);
    }

}
