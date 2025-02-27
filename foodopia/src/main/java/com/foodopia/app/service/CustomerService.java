package com.foodopia.app.service;

import com.foodopia.app.dto.RegistrationDto;
import com.foodopia.app.exceptions.UsernameAlreadyExistsException;
import com.foodopia.app.models.users.Customer;
import com.foodopia.app.domain.AbstractFoodopiaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.foodopia.app.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer registerNewCustomer(RegistrationDto registrationDto) {
        // Check if username is already taken
        customerRepository.findByUsername(registrationDto.getUsername())
                .ifPresent(u -> {
                    throw new UsernameAlreadyExistsException("Username already exists");
                });

        // Create a new Customer instance
        Customer customer = new Customer(
                registrationDto.getUsername(),
                passwordEncoder.encode(registrationDto.getPassword()),
                registrationDto.getEmail(),
                registrationDto.getUsername(),
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                AbstractFoodopiaUser.Role.CUSTOMER,
                registrationDto.getPhone(),
                registrationDto.getAddress()
        );

        return customerRepository.save(customer);
    }
}