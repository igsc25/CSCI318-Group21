package com.csci318.user.controller;

import com.csci318.user.controller.dto.AdminDTO;
import com.csci318.user.controller.dto.CustomerDTO;
import com.csci318.user.model.entity.Admin;
import com.csci318.user.model.entity.Customer;
import com.csci318.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Helpers ----------

    private AdminDTO toAdminDTO(Admin admin) {
        if (admin == null) {
            return null;
        }

        AdminDTO adminDTO = new AdminDTO();

        adminDTO.setUserID(admin.getUserID());
        adminDTO.setFirstName(admin.getFirstName());
        adminDTO.setLastName(admin.getLastName());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setPassword(admin.getPassword());
        adminDTO.setPasswordSalt(admin.getPasswordSalt());

        return adminDTO;
    }

    private Admin toAdminEntity(AdminDTO adminDTO) {
        if (adminDTO == null) {
            return null;
        }

        return Admin.createUser(
                adminDTO.getFirstName(),
                adminDTO.getLastName(),
                adminDTO.getEmail(),
                adminDTO.getPassword(),
                adminDTO.getPasswordSalt()
        );
    }

    private CustomerDTO toCustomerDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setUserID(customer.getUserID());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPassword(customer.getPassword());
        customerDTO.setPasswordSalt(customer.getPasswordSalt());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setAddresses(customer.getAddresses());

        return customerDTO;
    }

    private Customer toCustomerEntity(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return null;
        }

        return Customer.createUser(
                customerDTO.getFirstName(),
                customerDTO.getLastName(),
                customerDTO.getEmail(),
                customerDTO.getPassword(),
                customerDTO.getPasswordSalt(),
                customerDTO.getPhoneNumber(),
                customerDTO.getAddresses()
        );
    }

    // Admin ----------

    // No need for create
    // method because it is handled by auth service registration

    @GetMapping("/admins/{adminID}")
    AdminDTO findAdminByID(@PathVariable Long adminID) {
        Admin admin = userService.findAdminByID(adminID);
        return toAdminDTO(admin);
    }

    @PutMapping("/admins/{adminID}")
    AdminDTO updateAdmin(@PathVariable Long adminID, @Valid @RequestBody AdminDTO newAdmin) {
        Admin admin = userService.updateAdminInformation(adminID, toAdminEntity(newAdmin));
        return toAdminDTO(admin);
    }

    @DeleteMapping("/admins/{adminID}")
    void deleteAdmin(@PathVariable Long adminID) {
        userService.deleteAdmin(adminID);
    }

    // Customer ----------

    // No need for create method because it is handled by auth service registration

    @GetMapping("/customers/{customerID}")
    CustomerDTO findCustomerByID(@PathVariable Long customerID) {
        Customer customer = userService.findCustomerByID(customerID);
        return toCustomerDTO(customer);
    }

    @GetMapping("/customers")
    List<CustomerDTO> findAllCustomers() {
        List<Customer> customers = userService.findAllCustomers();
        return customers.stream().map(this::toCustomerDTO).toList();
    }

    @PutMapping("/customers/{customerID}")
    CustomerDTO updateCustomer(@PathVariable Long customerID, @Valid @RequestBody CustomerDTO newCustomer) {
        Customer customer = userService.updateCustomerInformation(customerID, toCustomerEntity(newCustomer));
        return toCustomerDTO(customer);
    }

    @DeleteMapping("/customers/{customerID}")
    void deleteCustomer(@PathVariable Long customerID) {
        userService.deleteCustomer(customerID);
    }
}
