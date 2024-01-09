package com.switchfully.eurder_db.service;

import com.switchfully.eurder_db.dto.*;
import com.switchfully.eurder_db.entity.Customer;
import com.switchfully.eurder_db.entity.Item;
import com.switchfully.eurder_db.exception.*;
import com.switchfully.eurder_db.mapper.CustomerMapper;
import com.switchfully.eurder_db.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }



    public Customer authenticate(String email, String password) throws UnknownAdminEmailException, WrongPasswordException {
        return validatePassword(findByEmail(email), password);
    }

    private Customer validatePassword(Customer customer, String password) throws WrongPasswordException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(password, customer.getPassword())) {
            throw new WrongPasswordException();
        }

        return customer;
    }

    private Customer findByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(UnknownCustomerEmailException::new);
    }



    public CustomerDto createCustomer(CreateCustomerDto createCustomerDto) {
        createCustomerDto.setPassword(bCryptPasswordEncoder.encode(createCustomerDto.getPassword()));
        Customer customer = customerMapper.createCustomerDtoToCustomer(createCustomerDto);

        return customerMapper.customerToCustomerDto(customerRepository.save(customer));
    }

    public CustomerDto updateCustomer(Long id, UpdateCustomerDto updateCustomerDto) throws UnknownCustomerIdException {
        Customer customer = customerMapper.updateCustomerDtoToCustomer(customerRepository.findById(id).orElseThrow(UnknownCustomerIdException::new), updateCustomerDto);

        return customerMapper.customerToCustomerDto(customerRepository.save(customer));
    }

    public CustomerDto findById(Long id) throws UnknownCustomerIdException {
        Customer customer = customerRepository.findById(id).orElseThrow(UnknownCustomerIdException::new);

        return customerMapper.customerToCustomerDto(customer);
    }

    public List<CustomerDto> findAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto).collect(Collectors.toList());
    }

    public void checkId(Customer customer, Long id) throws WrongCustomerIdException {
        if (!Objects.equals(customer.getId(), id)) {
            throw new WrongCustomerIdException();
        }
    }
}
