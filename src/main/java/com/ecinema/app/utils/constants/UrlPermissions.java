package com.ecinema.app.utils.constants;

/**
 * The type Url permissions.
 */
public class UrlPermissions {

    /**
     * The constant ANY_PERMITTED.
     */
    public static final String[] ANY_PERMITTED = new String[] {
            "/",
            "/index*",
            "/home*",
            "/login*"
    };

    public static final String[] AUTHENTICATED_PERMITTED = new String[] {
            "/authenticated"
    };

    /**
     * The constant CUSTOMERS_PERMITTED.
     */
    public static final String[] CUSTOMERS_PERMITTED = new String[] {
            "/customer"
    };

    /**
     * The constant MODERATORS_PERMITTED.
     */
    public static final String[] MODERATORS_PERMITTED = new String[] {
            "/moderator"
    };

    /**
     * The constant ADMIN_TRAINEES_PERMITTED.
     */
    public static final String[] ADMIN_TRAINEES_PERMITTED = new String[] {
            "/trainee"
    };

    /**
     * The constant ADMINS_PERMITTED.
     */
    public static final String[] ADMINS_PERMITTED = new String[] {
            "/admin"
    };

}
