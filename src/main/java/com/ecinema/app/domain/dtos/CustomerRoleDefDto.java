package com.ecinema.app.domain.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRoleDefDto implements AbstractDto {
    private Long userId;
    private Boolean isCensored;
}
