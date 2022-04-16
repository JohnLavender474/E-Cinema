package com.ecinema.app.security;

public class AppUrlPermissions {
    public static final String[] ANY = new String[] {
            "/",
            "/home",
            "/css/*",
            "js/*"
    };
    public static final String[] CUSTOMER = new String[] {
            "/profile"
    };
}
