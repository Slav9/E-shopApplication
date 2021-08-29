package com.example.ShopCart.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, VENDOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
