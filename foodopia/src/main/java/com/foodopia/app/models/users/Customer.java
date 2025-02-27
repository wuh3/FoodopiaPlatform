package com.foodopia.app.models.users;

import com.foodopia.app.domain.AbstractFoodopiaUser;
import com.mongodb.lang.Nullable;

public class Customer extends AbstractFoodopiaUser {
    @Nullable
    private String alias;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String phone;
    @Nullable
    private String address;

    public Customer(
            String username,
            String password,
            String email,
            @Nullable String alias,
            @Nullable String firstName,
            @Nullable String lastName,
            Role role,
            @Nullable String phone,
            @Nullable String address) {
        super(username, password, email, role);
        this.alias = alias;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    @Nullable
    private String getAlias() {
        return alias;
    }

    private void setAlias(@Nullable String alias) {
        this.alias = alias;
    }

    @Nullable
    private String getFirstName() {
        return firstName;
    }

    private void setFirstName(@Nullable String firstName) {
        this.firstName = firstName;
    }

    @Nullable
    private String getLastName() {
        return lastName;
    }

    private void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }
}