package com.ecinema.app.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserAuthorityDto implements AbstractDto {
    private Long userId = 0L;
    private Long authorityId = 0L;
    private String email = "";
    private String username = "";
}
