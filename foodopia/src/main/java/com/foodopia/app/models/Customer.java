package com.foodopia.app.models;

import com.foodopia.app.domain.AbstractFoodopiaUser;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Customer")
public class Customer extends AbstractFoodopiaUser {

    public Customer(String username, String password, String alias, String firstName, String lastName, Role role) {
        super(username, password, alias, firstName, lastName, role);
    }
}
