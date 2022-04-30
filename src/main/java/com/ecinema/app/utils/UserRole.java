package com.ecinema.app.utils;

import com.ecinema.app.entities.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

/**
 * Enumeration of each of the roles assignable to {@link User}. There is a one-to-one association between
 * each UserRole enum value and a child class of {@link UserRoleDef}.
 */
public enum UserRole implements GrantedAuthority {

    /** The Admin. */
    ADMIN {

        @Override
        @SuppressWarnings("unchecked")
        public AdminRoleDef instantiateNew() {
            return new AdminRoleDef();
        }

        @Override
        @SuppressWarnings("unchecked")
        public AdminRoleDef castToDefType(Object o) {
            return (AdminRoleDef) o;
        }

    },

    /** The Admin trainee. */
    ADMIN_TRAINEE {
        @Override
        @SuppressWarnings("unchecked")
        public AdminTraineeRoleDef instantiateNew() {
            return new AdminTraineeRoleDef();
        }

        @Override
        @SuppressWarnings("unchecked")
        public AdminTraineeRoleDef castToDefType(Object o) {
            return (AdminTraineeRoleDef) o;
        }

    },

    /** The Moderator. */
    MODERATOR {

        @Override
        @SuppressWarnings("unchecked")
        public ModeratorRoleDef instantiateNew() {
            return new ModeratorRoleDef();
        }

        @Override
        @SuppressWarnings("unchecked")
        public ModeratorRoleDef castToDefType(Object o) {
            return (ModeratorRoleDef) o;
        }

    },

    /** The Customer. */
    CUSTOMER {

        @Override
        @SuppressWarnings("unchecked")
        public CustomerRoleDef instantiateNew() {
            return new CustomerRoleDef();
        }

        @Override
        @SuppressWarnings("unchecked")
        public CustomerRoleDef castToDefType(Object o) {
            return (CustomerRoleDef) o;
        }

    };

    /**
     * Instantiates a new {@link UserRoleDef} child instance associated with the enum value.
     *
     * @param <T> the UserRoleDef child type to be instantiated
     * @return the UserRoleDef child type to be instantiated
     */
    public abstract <T extends UserRoleDef> T instantiateNew();

    /**
     * Casts the provided object to the {@link UserRoleDef} child class associated with the UserRole enum value.
     * The cast is unchecked, and it is assumed the caller knows for sure the provided object is an instance of
     * the class it is being cast to.
     *
     * @param <T> the UserRoleDef child type to cast the object to
     * @param o   the object to be cast
     * @return the UserRoleDef child type
     */
    public abstract <T extends UserRoleDef> T castToDefType(Object o);

    @Override
    public String getAuthority() {
        return name();
    }

    private static final Map<Class<? extends UserRoleDef>, UserRole> DEF_CLASS_TO_USER_ROLE_MAP = new HashMap<>() {{
        put(AdminRoleDef.class, ADMIN);
        put(CustomerRoleDef.class, CUSTOMER);
        put(ModeratorRoleDef.class, MODERATOR);
        put(AdminTraineeRoleDef.class, ADMIN_TRAINEE);
    }};

    /**
     * Fetches the UserRole enum value associated with the provided {@link UserRoleDef} child class.
     *
     * @param userRoleDefClass the user role def class associated with the UserRole enum value to be fetched.
     * @return the user role associated with the provided UserRoleDef child class.
     */
    public static UserRole defClassToUserRole(Class<? extends UserRoleDef> userRoleDefClass) {
        return DEF_CLASS_TO_USER_ROLE_MAP.get(userRoleDefClass);
    }

}
