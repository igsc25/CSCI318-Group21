package com.csci318.user.model.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends EndUser {
    protected Admin() {}

    protected Admin(String firstName, String lastName, String email, String password, String passwordSalt) {
        super(firstName, lastName, email, password, passwordSalt);
    }

    // Factory methods
    public static Admin createUser(String firstName, String lastName, String email, String password, String passwordSalt) {
        return new Admin(firstName, lastName, email, password, passwordSalt);
    }
}
