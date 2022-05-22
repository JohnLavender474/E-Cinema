package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PaymentCardDto extends AbstractDto {
    private AddressDto billingAddress = null;
    private Long cardOwnerId = null;
    private String firstName = "";
    private String lastName = "";
    private String expirationDate = "";
}
