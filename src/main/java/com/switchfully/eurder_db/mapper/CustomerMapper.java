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

    public Customer updateCustomerDtoToCustomer(UpdateCustomerDto updateCustomerDto) {
        return new Customer(updateCustomerDto.getEmail(), updateCustomerDto.getPassword(), updateCustomerDto.getFirstName(), updateCustomerDto.getLastName(), updateCustomerDto.getPhoneNumber(), updateCustomerDto.getAddress());
    }
}
