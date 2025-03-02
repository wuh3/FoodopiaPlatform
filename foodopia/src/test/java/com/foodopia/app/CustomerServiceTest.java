package com.foodopia.app;

import com.foodopia.app.dto.RegistrationDto;
import com.foodopia.app.models.users.Customer;
import com.foodopia.app.repository.CustomerRepository;
import com.foodopia.app.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void cleanup() {
        customerRepository.findByUsername("testuser").ifPresent(customerRepository::delete);
    }

    @Test
    public void testRegisterNewCustomer() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setPassword("TestPass123");
        registrationDto.setEmail("testuser@example.com");
        registrationDto.setFirstName("Test");
        registrationDto.setLastName("User");
        registrationDto.setPhone("1234567890");
        registrationDto.setAddress("123 Test Street");

        Customer registeredCustomer = customerService.registerNewCustomer(registrationDto);

        assertNotNull(registeredCustomer, "Registered customer should not be null");
        assertNotEquals("TestPass123", registeredCustomer.getPassword(), "Password should be encoded");

        Customer retrievedCustomer = customerRepository.findByUsername("testuser").orElse(null);
        assertNotNull(retrievedCustomer, "Customer should be retrievable from the repository");
        assertEquals("testuser", retrievedCustomer.getUsername(), "Username should match");
        assertEquals("testuser@example.com", retrievedCustomer.getEmail(), "Email should match");
    }

    @Test
    public void testRegistrationFormat() {
        RegistrationDto registrationDto = new RegistrationDto();
    }
}