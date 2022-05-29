package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.AbstractDto;
import com.ecinema.app.util.UtilMethods;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChangePasswordDto extends AbstractDto {

    private Long userId;
    private String email;
    private String token;
    private LocalDateTime creationDateTime;
    private LocalDateTime expirationDateTime;

    public String creationDateTimeFormatted() {
        return UtilMethods.localDateTimeFormatted(creationDateTime);
    }

    public String expirationDateTimeFormatted() {
        return UtilMethods.localDateTimeFormatted(expirationDateTime);
    }

}
