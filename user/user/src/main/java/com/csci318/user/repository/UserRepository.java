package com.csci318.user.repository;

import com.csci318.user.model.entity.Admin;
import com.csci318.user.model.entity.Customer;
import com.csci318.user.model.entity.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<EndUser, Long> {
    @Query("SELECT a FROM Admin a WHERE a.email = ?1")
    List<Admin> findAdminsByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    List<Customer> findCustomerByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c")
    List<Customer> findAllCustomers();
}
