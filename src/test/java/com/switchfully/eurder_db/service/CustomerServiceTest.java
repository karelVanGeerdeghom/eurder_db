package com.switchfully.eurder_db.service;

import com.switchfully.eurder_db.entity.Customer;
import com.switchfully.eurder_db.dto.CreateCustomerDto;
import com.switchfully.eurder_db.dto.CustomerDto;
import com.switchfully.eurder_db.exception.UnknownCustomerEmailException;
import com.switchfully.eurder_db.exception.UnknownCustomerIdException;
import com.switchfully.eurder_db.exception.WrongPasswordException;
import com.switchfully.eurder_db.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class CustomerServiceTest {
    private Customer customer;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        customer = customerRepository.save(new Customer("customer@eurder_db.com", bCryptPasswordEncoder.encode("customer"), "firstName", "lastName", "phoneNumber", "address"));
    }

    @Test
    void givenRightEmailAndRightPassword_whenAuthenticate_thenGetAuthenticatedCustomer() {
        // GIVEN
        String email = "customer@eurder_db.com";
        String password = "customer";

        // WHEN
        Customer actual = customerService.authenticate(email, password);

        // THEN
        assertThat(actual).isInstanceOf(Customer.class);
    }

    @Test
    void givenRightEmailAndWrongPassword_whenAuthenticate_thenThrowWrongPasswordException() {
        // GIVEN
        String email = "customer@eurder_db.com";
        String password = "password";

        // WHEN + THEN
        assertThatThrownBy(() -> customerService.authenticate(email, password)).isInstanceOf(WrongPasswordException.class);
    }

    @Test
    void givenWrongEmailAndRightPassword_whenAuthenticate_thenThrowUnknownCustomerEmailException() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "customer";

        // WHEN + THEN
        assertThatThrownBy(() -> customerService.authenticate(email, password)).isInstanceOf(UnknownCustomerEmailException.class);
    }

    @Test
    void givenWrongEmailAndWrongPassword_whenAuthenticate_thenThrowUnknownCustomerEmailException() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "password";

        // WHEN + THEN
        assertThatThrownBy(() -> customerService.authenticate(email, password)).isInstanceOf(UnknownCustomerEmailException.class);
    }

    @Test
    void givenCreateCustomerDto_whenCreateCustomer_thenGetCustomerDto() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneNumber = "phoneNumber";
        String address = "address";
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(email, password, firstName, lastName, phoneNumber, address);

        // WHEN
        CustomerDto actual = customerService.createCustomer(createCustomerDto);

        // THEN
        assertThat(actual).isInstanceOf(CustomerDto.class);
        assertThat(actual.getId()).isEqualTo(2);
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getFirstName()).isEqualTo(firstName);
        assertThat(actual.getLastName()).isEqualTo(lastName);
        assertThat(actual.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(actual.getAddress()).isEqualTo(address);
    }

    @Test
    void givenExistingId_whenGetCustomerById_thenGetCustomerWithGivenId() {
        // GIVEN
        Long id = 1L;

        // WHEN
        CustomerDto actual = customerService.findById(id);

        // THEN
        assertThat(actual).isInstanceOf(CustomerDto.class);
        assertThat(actual.getId()).isEqualTo(id);
    }

    @Test
    void givenWrongId_whenGetCustomerById_thenThrowUnknownCustomerIdException() {
        // GIVEN
        Long id = 2L;

        // WHEN + THEN
        assertThatThrownBy(() -> customerService.findById(id)).isInstanceOf(UnknownCustomerIdException.class);
    }

    @Test
    void whenGetAllCustomers_thenGetAllCustomerDtos() {
        // WHEN
        List<CustomerDto> actual = customerService.findAllCustomers();

        // THEN
        assertThat(actual).satisfiesExactly(customerDto -> {
            assertThat(customerDto.getId()).isEqualTo(customer.getId());
            assertThat(customerDto.getEmail()).isEqualTo(customer.getEmail());
            assertThat(customerDto.getFirstName()).isEqualTo(customer.getFirstName());
            assertThat(customerDto.getLastName()).isEqualTo(customer.getLastName());
            assertThat(customerDto.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
            assertThat(customerDto.getAddress()).isEqualTo(customer.getAddress());
        });
    }
}