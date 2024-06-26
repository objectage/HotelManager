package com.Manager.Hotel.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import com.Manager.Hotel.repository.CustomerRepository;
import com.Manager.Hotel.entity.Customer;

@Service
public class CustomerService {


    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }   

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> getCustomersByName(String name) {
        return customerRepository.findByName(name);
    }

    public Boolean checkCustomer(String name, String password) {
        List<Customer> customers = customerRepository.findByEmail(name);
        for (Customer customer : customers) {
            if (customer.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public Customer getCustomerByEmail(String email) {
        List<Customer> customers = customerRepository.findByEmail(email);
        if (customers.size() > 0) {
            return customers.get(0);
        }
        return null;
    }

}
