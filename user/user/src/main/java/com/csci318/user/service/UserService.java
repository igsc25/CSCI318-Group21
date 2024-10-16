package com.csci318.user.service;

import com.csci318.user.model.entity.Admin;
import com.csci318.user.model.entity.Customer;
import com.csci318.user.model.event.LoginEvent;
import com.csci318.user.model.event.RegistrationEvent;
import com.csci318.user.producer.UserEventProducer;
import com.csci318.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    public UserService(UserRepository userRepository, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    // Admin methods ----------

    public void createAdmin(Admin newAdmin) {
        if (userRepository.existsByEmail(newAdmin.getEmail())) {
            RegistrationEvent registrationEvent = new RegistrationEvent("registrationFailed", newAdmin.getEmail());
            userEventProducer.publishRegistrationFailedEvent(registrationEvent);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with the same ID already exists");
        }

        Admin admin = Admin.createUser(
                newAdmin.getFirstName(),
                newAdmin.getLastName(),
                newAdmin.getEmail(),
                newAdmin.getPassword(),
                newAdmin.getPasswordSalt()
        );

        userRepository.save(admin);

        RegistrationEvent registrationEvent = new RegistrationEvent("registrationSucceed", newAdmin.getEmail());
        userEventProducer.publishRegistrationSuccessEvent(registrationEvent);
    }

    public Admin findAdminByID(Long userID) {
        return userRepository.findById(userID)
                .filter(user -> user instanceof Admin)
                .map(user -> (Admin) user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
    }

    public Admin findAdminByEmail(String email) {
        return userRepository.findAdminsByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
    }

    public Admin updateAdminInformation(Long userID, Admin newAdmin) {
        Admin admin = findAdminByID(userID);
        admin.setFirstName(newAdmin.getFirstName());
        admin.setLastName(newAdmin.getLastName());
        admin.setEmail(newAdmin.getEmail());
        admin.setPassword(newAdmin.getPassword());

        return userRepository.save(admin);
    }

    public void deleteAdmin(Long userID) {
        Admin admin = findAdminByID(userID);
        userRepository.delete(admin);
    }

    // Customer methods ----------

    public void createCustomer(Customer newCustomer) {
        if (userRepository.existsByEmail(newCustomer.getEmail())) {
            RegistrationEvent registrationEvent = new RegistrationEvent("registrationFailed", newCustomer.getEmail());
            userEventProducer.publishRegistrationFailedEvent(registrationEvent);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with the same ID already exists");
        }

        Customer customer = Customer.createUser(
                newCustomer.getFirstName(),
                newCustomer.getLastName(),
                newCustomer.getEmail(),
                newCustomer.getPassword(),
                newCustomer.getPasswordSalt(),
                newCustomer.getPhoneNumber(),
                newCustomer.getAddresses()
        );

        userRepository.save(customer);

        RegistrationEvent registrationEvent = new RegistrationEvent("registrationSucceed", newCustomer.getEmail());
        userEventProducer.publishRegistrationSuccessEvent(registrationEvent);
    }

    public Customer findCustomerByID(Long userID) {
        return userRepository.findById(userID)
                .filter(user -> user instanceof Customer)
                .map(user -> (Customer) user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    public Customer findCustomerByEmail(String email) {
        return userRepository.findCustomerByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    public List<Customer> findAllCustomers() {
        return userRepository.findAllCustomers();
    }

    public Customer updateCustomerInformation(Long userID, Customer newCustomer) {
        Customer customer = findCustomerByID(userID);
        customer.setFirstName(newCustomer.getFirstName());
        customer.setLastName(newCustomer.getLastName());
        customer.setEmail(newCustomer.getEmail());
        customer.setPassword(newCustomer.getPassword());
        customer.setPhoneNumber(newCustomer.getPhoneNumber());
        customer.setAddresses(newCustomer.getAddresses());

        return userRepository.save(customer);
    }

    public void deleteCustomer(Long userID) {
        Customer customer = findCustomerByID(userID);
        userRepository.delete(customer);
    }

    // Other ----------

    public void retrievePassword(String email, String inputPassword, String role) {
        String databasePassword;
        String passwordSalt;

        if (role.equals("ADMIN")) {
            Admin admin = findAdminByEmail(email);
            databasePassword = admin.getPassword();
            passwordSalt = admin.getPasswordSalt();
        } else {
            Customer customer = findCustomerByEmail(email);
            databasePassword = customer.getPassword();
            passwordSalt = customer.getPasswordSalt();
        }

        LoginEvent loginEvent = new LoginEvent("passwordRetrieved", email, inputPassword, databasePassword, passwordSalt, role);
        userEventProducer.publishPasswordRetrievedEvent(loginEvent);
    }
}
