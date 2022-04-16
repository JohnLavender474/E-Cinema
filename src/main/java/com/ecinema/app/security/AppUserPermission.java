package com.ecinema.app.security;

public enum AppUserPermission {

    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_DELETE("admin:delete"),

    COUPON_READ("coupon:read"),
    COUPON_WRITE("coupon:write"),
    COUPON_DELETE("coupon:delete"),

    CUSTOMER_READ("customer:read"),
    CUSTOMER_WRITE("customer:write"),
    CUSTOMER_DELETE("customer:delete"),

    MOVIE_READ("movie:read"),
    MOVIE_WRITE("movie:write"),
    MOVIE_DELETE("movie:delete"),

    PAYMENTCARD_READ("paymentCard:read"),
    PAYMENTCARD_WRITE("paymentCard:write"),
    PAYMENTCARD_DELETE("paymentCard:delete"),

    REVIEW_READ("review:read"),
    REVIEW_WRITE("review:write"),
    REVIEW_DELETE("review:delete"),

    SCREENING_READ("screening:read"),
    SCREENING_WRITE("screening:write"),
    SCREENING_DELETE("screening:delete"),

    SHOWROOM_READ("showroom:read"),
    SHOWROOM_WRITE("showroom:write"),
    SHOWROOM_DELETE("showroom:delete"),

    TICKET_READ("ticket:read"),
    TICKET_WRITE("ticket:write"),
    TICKET_DELETE("ticket:delete"),

    USER_ACCOUNT_DETAILS_READ("userAccountDetails:read"),
    USER_ACCOUNT_DETAILS_WRITE("userAccountDetails:write"),
    USER_ACCOUNT_DETAILS_DELETE("userAccountDetails:delete");

    private final String permission;

    AppUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
