package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.UserAuthorityDto;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.entities.AbstractUserAuthority;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.UserAuthorityRepository;
import com.ecinema.app.util.UtilMethods;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ToString
@Transactional
public abstract class UserAuthorityService<E extends AbstractUserAuthority, R extends UserAuthorityRepository<E>,
        D extends UserAuthorityDto> extends AbstractEntityService<E, R, D> {

    public UserAuthorityService(R repository) {
        super(repository);
    }

    @Override
    protected void onDelete(E userAuthority) {
        User user = userAuthority.getUser();
        logger.debug("Before detach user authority from user: " + user);
        if (user != null) {
            user.getUserAuthorities().remove(userAuthority.getUserAuthority());
            userAuthority.setUser(null);
        }
        logger.debug("After detach user authority from user: " + user);
    }

    public void fillCommonUserAuthorityDtoFields(E userAuthority, D dto) {
        dto.setId(userAuthority.getId());
        dto.setUserId(userAuthority.getUser().getId());
        dto.setEmail(userAuthority.getUser().getEmail());
        dto.setUsername(userAuthority.getUser().getUsername());
    }

    public Optional<D> findByUserWithId(Long userId) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Find by user with id: " + userId);
        E authority = repository.findByUserWithId(userId).orElse(null);
        logger.debug("Found user: " + authority);
        if (authority == null) {
            return Optional.empty();
        }
        return Optional.of(convertToDto(authority));
    }

    public Long findIdByUserWithId(Long userId)
            throws NoEntityFoundException {
        return repository.findIdByUserWithId(userId).orElseThrow(
                () -> new NoEntityFoundException("user authority id", "user id", userId));
    }

}
