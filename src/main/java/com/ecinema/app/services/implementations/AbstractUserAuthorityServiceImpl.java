package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.entities.AbstractUserAuthority;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.AbstractUserAuthorityRepository;
import com.ecinema.app.services.AbstractUserAuthorityService;

import java.util.Collection;
import java.util.Optional;

/**
 * The type User role def service.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public abstract class AbstractUserAuthorityServiceImpl<T extends AbstractUserAuthority,
        R extends AbstractUserAuthorityRepository<T>> extends AbstractServiceImpl<T, R>
        implements AbstractUserAuthorityService<T> {

    /**
     * Instantiates a new User role def service.
     *
     * @param repository the repository
     */
    public AbstractUserAuthorityServiceImpl(R repository) {
        super(repository);
    }

    @Override
    protected void onDelete(T AbstractUserAuthority) {
        User user = AbstractUserAuthority.getUser();
        logger.debug("Detach user role def from user: " + user);
        if (user != null) {
            user.getUserAuthorities().remove(AbstractUserAuthority.getUserAuthority());
            AbstractUserAuthority.setUser(null);
        }
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info) {
        T AbstractUserAuthority = findById(id).orElseThrow(
                () -> new NoEntityFoundException("user role def", "id", id));
        onDeleteInfo(AbstractUserAuthority, info);
    }

    @Override
    public void onDeleteInfo(T AbstractUserAuthority, Collection<String> info) {
        String username = AbstractUserAuthority.getUser().getUsername();
        info.add(username + " will lose authority: " + AbstractUserAuthority.getClass().getSimpleName());
    }

    @Override
    public Optional<T> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public Optional<T> findByUserWithId(Long userId) {
        return repository.findByUserWithId(userId);
    }

    @Override
    public Optional<Long> findIdByUser(User user) {
        return repository.findIdByUser(user);
    }

    @Override
    public Optional<Long> findIdByUserWithId(Long userId) {
        return repository.findIdByUserWithId(userId);
    }

}
