package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.*;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.*;
import com.ecinema.app.utils.constants.UserRole;
import com.ecinema.app.utils.exceptions.ClashesWithExistentObjectException;
import com.ecinema.app.utils.exceptions.InvalidArgException;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl extends AbstractServiceImpl<User, UserRepository> implements UserService {

    private final AdminRoleDefService adminRoleDefService;
    private final CustomerRoleDefService customerRoleDefService;
    private final ModeratorRoleDefService moderatorRoleDefService;
    private final AdminTraineeRoleDefService adminTraineeRoleDefService;

    public UserServiceImpl(UserRepository repository,
                           CustomerRoleDefService customerRoleDefService,
                           ModeratorRoleDefService moderatorRoleDefService,
                           AdminTraineeRoleDefService adminTraineeRoleDefService,
                           AdminRoleDefService adminRoleDefService) {
        super(repository);
        this.customerRoleDefService = customerRoleDefService;
        this.moderatorRoleDefService = moderatorRoleDefService;
        this.adminTraineeRoleDefService = adminTraineeRoleDefService;
        this.adminRoleDefService = adminRoleDefService;

    }

    @Override
    protected void onDelete(User user) {

    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("No user found with email " + email));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public <T extends UserRoleDef> Optional<T> getUserRoleDefOf(Long userId, Class<T> userRoleDefClass)
            throws InvalidArgException, NoEntityFoundException {
        UserRole userRole = UserRole.defClassToUserRole(userRoleDefClass);
        if (userRole == null) {
            throw new InvalidArgException("The provided class " + userRoleDefClass.getName() +
                    " is not mapped to a user role value");
        }
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("User", "id", userId));
        UserRoleDef userRoleDef = user.getUserRoleDefs().get(userRole);
        return Optional.ofNullable(userRoleDefClass.cast(userRoleDef));
    }

    @Override
    public List<User> findAllByIsAccountLocked(boolean isAccountLocked) {
        return repository.findAllByIsAccountLocked(isAccountLocked);
    }

    @Override
    public List<User> findAllByIsAccountEnabled(boolean isAccountEnabled) {
        return repository.findAllByIsAccountEnabled(isAccountEnabled);
    }

    @Override
    public List<User> findAllByIsAccountExpired(boolean isAccountExpired) {
        return repository.findAllByIsAccountExpired(isAccountExpired);
    }

    @Override
    public List<User> findAllByIsCredentialsExpired(boolean isCredentialsExpired) {
        return repository.findAllByIsCredentialsExpired(isCredentialsExpired);
    }

    @Override
    public List<User> findAllByCreationDateTimeBefore(LocalDateTime localDateTime) {
        return repository.findAllByCreationDateTimeBefore(localDateTime);
    }

    @Override
    public List<User> findAllByCreationDateTimeAfter(LocalDateTime localDateTime) {
        return repository.findAllByCreationDateTimeAfter(localDateTime);
    }

    @Override
    public List<User> findAllByLastActivityDateTimeBefore(LocalDateTime localDateTime) {
        return repository.findAllByLastActivityDateTimeBefore(localDateTime);
    }

    @Override
    public List<User> findAllByLastActivityDateTimeAfter(LocalDateTime localDateTime) {
        return repository.findAllByLastActivityDateTimeAfter(localDateTime);
    }

    @Override
    public void addUserRoleDefToUser(Long userId, UserRole userRole)
            throws NoEntityFoundException, ClashesWithExistentObjectException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("user", "id", userId));
        switch (userRole) {
            case CUSTOMER -> {
                if (user.getUserRoleDefs().get(UserRole.CUSTOMER) != null) {
                    throw new ClashesWithExistentObjectException("User already has customer authority");
                }
                CustomerRoleDef customerRoleDef = new CustomerRoleDef();
                customerRoleDef.setUser(user);
                user.getUserRoleDefs().put(UserRole.CUSTOMER, customerRoleDef);
                customerRoleDefService.save(customerRoleDef);
            }
            case MODERATOR -> {
                if (user.getUserRoleDefs().get(UserRole.MODERATOR) != null) {
                    throw new ClashesWithExistentObjectException("User already has moderator authority");
                }
                ModeratorRoleDef moderatorRoleDef = new ModeratorRoleDef();
                moderatorRoleDef.setUser(user);
                user.getUserRoleDefs().put(UserRole.MODERATOR, moderatorRoleDef);
                moderatorRoleDefService.save(moderatorRoleDef);
            }
            case ADMIN_TRAINEE -> {
                if (user.getUserRoleDefs().get(UserRole.ADMIN_TRAINEE) != null) {
                    throw new ClashesWithExistentObjectException("User already has admin trainee authority");
                }
                AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
                adminTraineeRoleDef.setUser(user);
                user.getUserRoleDefs().put(UserRole.ADMIN_TRAINEE, adminTraineeRoleDef);
                adminTraineeRoleDefService.save(adminTraineeRoleDef);
            }
            case ADMIN -> {
                if (user.getUserRoleDefs().get(UserRole.ADMIN) != null) {
                    throw new ClashesWithExistentObjectException("User already has admin authority");
                }
                AdminRoleDef adminRoleDef = new AdminRoleDef();
                adminRoleDef.setUser(user);
                user.getUserRoleDefs().put(UserRole.ADMIN_TRAINEE, adminRoleDef);
                adminRoleDefService.save(adminRoleDef);
            }
            default -> throw new InvalidArgException("Provided value of user role is invalid");
        }
    }

    @Override
    public void removeUserRoleDefFromUser(Long userId, UserRole userRole)
            throws NoEntityFoundException, InvalidArgException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("User", "id", userId));
        switch (userRole) {
            case ADMIN -> {
                AdminRoleDef adminRoleDef = userRole.castToDefClass(user.getUserRoleDefs().get(userRole));
                adminRoleDefService.deleteById(adminRoleDef.getId());
            }
            case CUSTOMER -> {
                CustomerRoleDef customerRoleDef = userRole.castToDefClass(user.getUserRoleDefs().get(userRole));
                customerRoleDefService.deleteById(customerRoleDef.getId());
            }
            case MODERATOR -> {
                ModeratorRoleDef moderatorRoleDef = userRole.castToDefClass(user.getUserRoleDefs().get(userRole));
                moderatorRoleDefService.deleteById(moderatorRoleDef.getId());
            }
            case ADMIN_TRAINEE -> {
                AdminTraineeRoleDef adminTraineeRoleDef = userRole.castToDefClass(user.getUserRoleDefs().get(userRole));
                adminTraineeRoleDefService.deleteById(adminTraineeRoleDef.getId());
            }
            default -> throw new InvalidArgException("The the provided value of user role " + userRole + " is invalid");
        }
    }

}
