package com.foodopia.app.domain;

import com.mongodb.lang.NonNull;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("AbstractFoodopiaUser")
public abstract class AbstractFoodopiaUser {
    @Id
    private Long _id;
    protected String username;
    protected String password;
    protected String alias;
    protected String firstName;
    protected String lastName;
    protected Role role;

    public enum Role {
        ADMIN,
        CUSTOMER,
        KITCHEN
    }

    public AbstractFoodopiaUser(
            @NonNull String username,
            @NonNull String password,
            @DefaultValue("UnknownUser") String alias,
            String firstName,
            String lastName,
            @NonNull Role role) {
        this.username = username;
        this.password = password;
        this.alias = alias;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    protected String getUsername() {
        return username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    protected String getAlias() {
        return alias;
    }

    protected void setAlias(String alias) {
        this.alias = alias;
    }

    protected String getFirstName() {
        return firstName;
    }

    protected void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    protected String getLastName() {
        return lastName;
    }

    protected void setLastName(String lastName) {
        this.lastName = lastName;
    }

    protected Role getRole() {
        return role;
    }

    protected void setRole(Role role) {
        this.role = role;
    }

}
