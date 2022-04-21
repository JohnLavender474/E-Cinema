package com.ecinema.app.utils.constants;

import com.ecinema.app.entities.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

public enum UserRole implements GrantedAuthority {

    ADMIN {

        @Override
        public String getAuthority() {
            return "ROLE_ADMIN";
        }

        @Override
        public Set<String> getPrivileges() {
            return new HashSet<>() {{
                    add(UserPermission.CUSTOMER_READ.getPermission());
                    add(UserPermission.CUSTOMER_WRITE.getPermission());
                    add(UserPermission.CUSTOMER_DELETE.getPermission());
            }};
        }

        @Override
        @SuppressWarnings("unchecked")
        public AdminRoleDef castToDefClass(Object o)
                throws ClassCastException {
            return getDefClass().cast(o);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<AdminRoleDef> getDefClass() {
            return AdminRoleDef.class;
        }

    },

    CUSTOMER {
        @Override
        public String getAuthority() {
            return "ROLE_CUSTOMER";
        }

        @Override
        public Set<String> getPrivileges() {
            return new HashSet<>() {{

            }};
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<CustomerRoleDef> getDefClass() {
            return CustomerRoleDef.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public CustomerRoleDef castToDefClass(Object o) {
            return getDefClass().cast(o);
        }
    },

    MODERATOR {

        @Override
        public String getAuthority() {
            return "ROLE_MODERATOR";
        }

        @Override
        public Set<String> getPrivileges() {
            return new HashSet<>() {{

            }};
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<ModeratorRoleDef> getDefClass() {
            return ModeratorRoleDef.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public ModeratorRoleDef castToDefClass(Object o) {
            return getDefClass().cast(o);
        }

    },

    ADMIN_TRAINEE {

        @Override
        public String getAuthority() {
            return "ROLE_ADMIN_TRAINEE";
        }

        @Override
        public Set<String> getPrivileges() {
            return new HashSet<>() {{

            }};
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<AdminTraineeRoleDef> getDefClass() {
            return AdminTraineeRoleDef.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public AdminTraineeRoleDef castToDefClass(Object o) {
            return getDefClass().cast(o);
        }

    };

    public abstract String getAuthority();
    public abstract Set<String> getPrivileges();
    public abstract <T extends UserRoleDef> Class<T> getDefClass();
    public abstract <T extends UserRoleDef> T castToDefClass(Object o);

    private static final Map<Class<? extends UserRoleDef>, UserRole> DEF_CLASS_TO_USER_ROLE_MAP = new HashMap<>() {{
        put(AdminRoleDef.class, ADMIN);
        put(CustomerRoleDef.class, CUSTOMER);
        put(ModeratorRoleDef.class, MODERATOR);
        put(AdminTraineeRoleDef.class, ADMIN_TRAINEE);
    }};

    public static UserRole defClassToUserRole(Class<? extends UserRoleDef> userRoleDefClass) {
        return DEF_CLASS_TO_USER_ROLE_MAP.get(userRoleDefClass);
    }

}
