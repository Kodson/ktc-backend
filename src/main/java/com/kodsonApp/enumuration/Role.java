package com.kodsonApp.enumuration;

import static com.kodsonApp.constant.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_HR(HR_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SAFETY_MANAGER(ADMIN_AUTHORITIES),

    ROLE_JOURNEY_PLANNER(ADMIN_AUTHORITIES),
    ROLE_FRONT_DESK(ADMIN_AUTHORITIES),
    ROLE_MAINTENANCE(ADMIN_AUTHORITIES),
    ROLE_FAM(ADMIN_AUTHORITIES),

    ROLE_CFO(ADMIN_AUTHORITIES),
    ROLE_OPERATIONS(ADMIN_AUTHORITIES),
    ROLE_GM(ADMIN_AUTHORITIES),

    ROLE_STATION_MANAGER(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);
    private String[] authorities;
    Role(String... authorities) {
        this.authorities = authorities;
    }
    public String[] getAuthorities() {
        return authorities;
    }
}
