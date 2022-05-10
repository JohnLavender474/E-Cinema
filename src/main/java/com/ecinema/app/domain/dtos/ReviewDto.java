package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.IReview;
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
        StringBuilder sb = new StringBuilder();
        sb.append(creationDateTime.getMonth()).append(" ")
          .append(creationDateTime.getDayOfMonth()).append(", ")
          .append(creationDateTime.getYear()).append(", ");
        String append = "am";
        int hour = creationDateTime.getHour();
        if (hour >= 12) {
            append = "pm";
        }
        if (hour > 12) {
            hour -= 12;
        } else if (hour == 0) {
            hour = 12;
        }
        sb.append(hour).append(":").append(creationDateTime.getMinute()).append(append);
        return sb.toString();
    }

}
