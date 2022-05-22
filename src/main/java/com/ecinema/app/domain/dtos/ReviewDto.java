package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.AbstractDto;
import com.ecinema.app.domain.contracts.IReview;
import com.ecinema.app.util.UtilMethods;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * The type Review dto.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReviewDto extends AbstractDto implements IReview {

    private Long customerId = null;
    private String writer = "";
    private String review = "";
    private Integer rating = 0;
    private LocalDateTime creationDateTime;
    private Boolean isCensored = false;
    private Boolean isReportedByCurrentLoggedInUser = false;
    private Boolean isVotedByCurrentLoggedInUser = false;

    /**
     * Creation date time formatted string.
     *
     * @return the string
     */
    public String creationDateTimeFormatted() {
        return UtilMethods.localDateTimeFormatted(creationDateTime);
    }

}
