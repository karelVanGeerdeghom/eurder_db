package com.switchfully.eurder_db.mapper;

import com.switchfully.eurder_db.entity.Customer;
import com.switchfully.eurder_db.dto.CreateCustomerDto;
import com.switchfully.eurder_db.dto.CustomerDto;
import com.switchfully.eurder_db.dto.UpdateCustomerDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerMapperTest {
    private final CustomerMapper customerMapper = new CustomerMapper();

    @Test
    void givenCustomer_whenMapCustomerToCustomerDto_thenGetCustomerDto() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneNumber = "phoneNumber";
        String address = "address";
        Customer customer = new Customer(email, password, firstName, lastName, phoneNumber, address);

        // WHEN
        CustomerDto actual = customerMapper.customerToCustomerDto(customer);

        // THEN
        assertThat(actual).isInstanceOf(CustomerDto.class);
        assertThat(actual.getId()).isNull();
        assertThat(actual.getEmail()).isEqualTo(customer.getEmail());
        assertThat(actual.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(customer.getLastName());
        assertThat(actual.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        assertThat(actual.getAddress()).isEqualTo(customer.getAddress());
    }

    @Test
    void givenCreateCustomerDto_whenMapCreateCustomerDtoToCustomer_thenGetCustomer() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneNumber = "phoneNumber";
        String address = "address";
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(email, password, firstName, lastName, phoneNumber, address);

        // WHEN
        Customer actual = customerMapper.createCustomerDtoToCustomer(createCustomerDto);

        // THEN
        assertThat(actual).isInstanceOf(Customer.class);
        assertThat(actual.getId()).isNull();
        assertThat(actual.getEmail()).isEqualTo(createCustomerDto.getEmail());
        assertThat(actual.getFirstName()).isEqualTo(createCustomerDto.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(createCustomerDto.getLastName());
        assertThat(actual.getPhoneNumber()).isEqualTo(createCustomerDto.getPhoneNumber());
        assertThat(actual.getAddress()).isEqualTo(createCustomerDto.getAddress());
    }

    @Test
    void givenUpdateCustomerDto_whenMapUpdateCustomerDtoToCustomer_thenGetCustomer() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneNumber = "phoneNumber";
        String address = "address";
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto(email, password, firstName, lastName, phoneNumber, address);

        // WHEN
        Customer actual = customerMapper.updateCustomerDtoToCustomer(updateCustomerDto);

        // THEN
        assertThat(actual).isInstanceOf(Customer.class);
        assertThat(actual.getId()).isNull();
        assertThat(actual.getEmail()).isEqualTo(updateCustomerDto.getEmail());
        assertThat(actual.getFirstName()).isEqualTo(updateCustomerDto.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(updateCustomerDto.getLastName());
        assertThat(actual.getPhoneNumber()).isEqualTo(updateCustomerDto.getPhoneNumber());
        assertThat(actual.getAddress()).isEqualTo(updateCustomerDto.getAddress());
    }
}