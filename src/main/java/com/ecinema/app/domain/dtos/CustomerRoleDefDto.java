package com.ecinema.app.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerRoleDefDto implements AbstractDto {
    private Long id;
    private Long userId;
    private Boolean isCensored;
}
