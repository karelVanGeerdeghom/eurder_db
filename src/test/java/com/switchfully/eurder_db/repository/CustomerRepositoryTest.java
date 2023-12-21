package com.switchfully.eurder_db.repository;

import com.switchfully.eurder_db.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void givenCustomer_whenCreateCustomer_thenGetCreatedCustomerWithIdOne() {
        // GIVEN
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "firstName.lastName@mail.com";
        String password = "password";
        String phoneNumber = "phoneNumber";
        String address = "address";

        Customer customer = new Customer(email, password, firstName, lastName, phoneNumber, address);

        // WHEN
        Customer actual = customerRepository.save(customer);

        // THEN
        assertThat(actual).isInstanceOf(Customer.class);
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getPassword()).isEqualTo(password);
        assertThat(actual.getFirstName()).isEqualTo(firstName);
        assertThat(actual.getLastName()).isEqualTo(lastName);
        assertThat(actual.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(actual.getAddress()).isEqualTo(address);
    }

    @Test
    void givenExistingId_whenGetCustomerById_thenGetCustomerWithGivenId() {
        // GIVEN
        Long id = 1L;

        String email = "firstName.lastName@mail.com";
        String password = "password";

        customerRepository.save(new Customer(email, password, "firstName", "lastName", "phoneNumber", "address"));

        // WHEN
        Customer actual = customerRepository.findById(id).get();

        // THEN
        assertThat(actual).isInstanceOf(Customer.class);
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getPassword()).isEqualTo(password);
    }

    @Test
    void givenExistingEmail_whenGetCustomerByEmail_thenGetCustomerWithGivenEmail() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "password";

        customerRepository.save(new Customer(email, password, "firstName", "lastName", "phoneNumber", "address"));

        // WHEN
        Customer actual = customerRepository.findByEmail(email).get();

        // THEN
        assertThat(actual).isInstanceOf(Customer.class);
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getPassword()).isEqualTo(password);
    }

    @Test
    void whenGetAllCustomers_thenGetAllCustomers() {
        // WHEN
        List<Customer> actual = customerRepository.findAll();

        // THEN
        assertThat(actual).allSatisfy(customer -> assertThat(customer).isInstanceOf(Customer.class));
    }
}