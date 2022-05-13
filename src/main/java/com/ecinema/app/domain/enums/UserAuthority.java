package com.ecinema.app.domain.enums;

import com.ecinema.app.domain.entities.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of each of the roles assignable to {@link User}. There is a one-to-one association between
 * each UserAuthority enum value and a child class of {@link AbstractUserAuthority}.
 */
public enum UserAuthority implements GrantedAuthority {

    /**
     * The Admin.
     */
    ADMIN {
        @Override
        @SuppressWarnings("unchecked")
        public AdminAuthority instantiate() {
            return new AdminAuthority();
        }

        @Override
        @SuppressWarnings("unchecked")
        public AdminAuthority cast(Object o) {
            return (AdminAuthority) o;
        }

        @Override
        public String toString() {
            return "Admin Authority";
        }

    },

    /**
     * The Moderator.
     */
    MODERATOR {
        @Override
        @SuppressWarnings("unchecked")
        public ModeratorAuthority instantiate() {
            return new ModeratorAuthority();
        }

        @Override
        @SuppressWarnings("unchecked")
        public ModeratorAuthority cast(Object o) {
            return (ModeratorAuthority) o;
        }

        @Override
        public String toString() {
            return "Moderator Authority";
        }

    },

    /**
     * The Customer.
     */
    CUSTOMER {
        @Override
        @SuppressWarnings("unchecked")
        public CustomerAuthority instantiate() {
            return new CustomerAuthority();
        }

        @Override
        @SuppressWarnings("unchecked")
        public CustomerAuthority cast(Object o) {
            return (CustomerAuthority) o;
        }

        @Override
        public String toString() {
            return "Customer Authority";
        }

    };

    private static final Map<Class<? extends AbstractUserAuthority>, UserAuthority> DEF_CLASS_TO_USER_ROLE_MAP =
            new HashMap<>() {{
                put(AdminAuthority.class, ADMIN);
                put(CustomerAuthority.class, CUSTOMER);
                put(ModeratorAuthority.class, MODERATOR);
            }};

    /**
     * Fetches the UserAuthority enum value associated with the provided {@link AbstractUserAuthority} child class.
     *
     * @param AbstractUserAuthorityClass the user role def class associated with the
     *                                   UserAuthority enum value to be fetched.
     * @return the user role associated with the provided AbstractUserAuthority child class.
     */
    public static UserAuthority defClassToUserRole(Class<? extends AbstractUserAuthority> AbstractUserAuthorityClass) {
        return DEF_CLASS_TO_USER_ROLE_MAP.get(AbstractUserAuthorityClass);
    }

    /**
     * Instantiates a new {@link AbstractUserAuthority} child instance associated with the enum value.
     *
     * @param <T> the AbstractUserAuthority child type to be instantiated
     * @return the AbstractUserAuthority child type to be instantiated
     */
    public abstract <T extends AbstractUserAuthority> T instantiate();

    /**
     * Casts the provided object to the {@link AbstractUserAuthority} child class associated with the UserAuthority
     * enum value. The cast is unchecked, and it is assumed the caller knows for sure the provided object is an
     * instance of the class it is being cast to.
     *
     * @param <T> the AbstractUserAuthority child type to cast the object to
     * @param o   the object to be cast
     * @return the AbstractUserAuthority child type
     */
    public abstract <T extends AbstractUserAuthority> T cast(Object o);

    @Override
    public String getAuthority() {
        return name();
    }

}
