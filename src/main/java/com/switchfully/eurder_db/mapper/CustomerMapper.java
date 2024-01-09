package com.switchfully.eurder_db.mapper;

import com.switchfully.eurder_db.dto.CreateCustomerDto;
import com.switchfully.eurder_db.dto.CustomerDto;
import com.switchfully.eurder_db.dto.UpdateCustomerDto;
import com.switchfully.eurder_db.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerDto customerToCustomerDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getEmail(), customer.getFirstName(), customer.getLastName(), customer.getPhoneNumber(), customer.getAddress());
    }

    public Customer createCustomerDtoToCustomer(CreateCustomerDto createCustomerDto) {
        return new Customer(createCustomerDto.getEmail(), createCustomerDto.getPassword(), createCustomerDto.getFirstName(), createCustomerDto.getLastName(), createCustomerDto.getPhoneNumber(), createCustomerDto.getAddress());
    }

    public Customer updateCustomerDtoToCustomer(Customer customer, UpdateCustomerDto updateCustomerDto) {
        customer.setEmail(updateCustomerDto.getEmail());
        customer.setFirstName(updateCustomerDto.getFirstName());
        customer.setLastName(updateCustomerDto.getLastName());
        customer.setPhoneNumber(updateCustomerDto.getPhoneNumber());
        customer.setAddress(updateCustomerDto.getAddress());

        return customer;
    }
}
