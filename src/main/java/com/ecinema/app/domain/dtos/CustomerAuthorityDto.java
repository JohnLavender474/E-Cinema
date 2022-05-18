package com.ecinema.app.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerAuthorityDto extends UserAuthorityDto {
    private Long censorId = 0L;
    private Boolean isCensored = false;
}
