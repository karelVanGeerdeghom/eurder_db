package com.switchfully.eurder_db.service;

import com.switchfully.eurder_db.dto.CreateCustomerDto;
import com.switchfully.eurder_db.dto.CustomerDto;
import com.switchfully.eurder_db.entity.Customer;
import com.switchfully.eurder_db.exception.UnknownAdminEmailException;
import com.switchfully.eurder_db.exception.UnknownCustomerEmailException;
import com.switchfully.eurder_db.exception.UnknownCustomerIdException;
import com.switchfully.eurder_db.exception.WrongPasswordException;
import com.switchfully.eurder_db.mapper.CustomerMapper;
import com.switchfully.eurder_db.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public CustomerDto findById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(UnknownCustomerIdException::new);

        return customerMapper.customerToCustomerDto(customer);
    }

    public List<CustomerDto> findAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto).collect(Collectors.toList());
    }
}
