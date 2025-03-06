package com.foodopia.app.domain;

import com.mongodb.lang.NonNull;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@TypeAlias("AbstractFoodopiaUser")
public abstract class AbstractFoodopiaUser implements UserDetails {
    @Id
    private String _id;
    protected String username;
    protected String password;
    protected String email;
    protected Role role;

    public enum Role {
        ADMIN,
        CUSTOMER,
        KITCHEN,
        OPERATOR,
    }

    public AbstractFoodopiaUser(
            @NonNull String username,
            @NonNull String password,
            @NonNull String email,
            @NonNull Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getId() {return this._id;}

    protected void setUsername(String username) {
        this.username = username;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {return this.email;}

    protected Role getRole() {
        return role;
    }

    protected void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Provide role-based authority, e.g., ROLE_CUSTOMER
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + getRole().name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Customize as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Customize as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Customize as needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Customize as needed
    }

}
