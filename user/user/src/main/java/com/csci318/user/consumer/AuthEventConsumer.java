package com.csci318.user.consumer;

import com.csci318.user.model.entity.Admin;
import com.csci318.user.model.entity.Customer;
import com.csci318.user.service.TokenService;
import com.csci318.user.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class AuthEventConsumer {
    @Autowired
    private final UserService userService;

    @Autowired
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    public AuthEventConsumer(UserService userService, TokenService tokenService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    private void userRegistrationRequestedEvent(JsonNode jsonNode) {
        try {
            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();
            String passwordSalt = jsonNode.get("passwordSalt").asText();
            String role = jsonNode.get("role").asText();
            System.out.println("Event Received");

            if (role.equals("ADMIN") || role.equals("CUSTOMER")) {
                if (role.equals("ADMIN")) {
                    Admin newAdmin = Admin.createUser("", "", email, password, passwordSalt);
                    userService.createAdmin(newAdmin);
                }

                if (role.equals("CUSTOMER")) {
                    Customer newCustomer = Customer.createUser("", "", email, password, passwordSalt, "", null);
                    userService.createCustomer(newCustomer);
                }
            } else {
                System.out.println("Invalid role");
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    private void userLoginRequestedEvent(JsonNode jsonNode) {
        try {
            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();
            String role = jsonNode.get("role").asText();

            if (role.equals("ADMIN") || role.equals("CUSTOMER")) {
                if (role.equals("ADMIN")) {
                    userService.retrievePassword(email, password, "ADMIN");
                }

                if (role.equals("CUSTOMER")) {
                    userService.retrievePassword(email, password, "CUSTOMER");
                }
            } else {
                System.out.println("Invalid role");
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    private void tokenRetrievedEvent(JsonNode jsonNode) {
        try {
            String token = jsonNode.get("token").asText();
            tokenService.setToken(token);
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    @Bean
    public Consumer<String> authEventListener() {
        return message -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(message);

                String eventName = jsonNode.get("eventName").asText();

                switch (eventName) {
                    case "userRegistrationRequested":
                        userRegistrationRequestedEvent(jsonNode);
                        break;
                    case "userLoginRequested":
                        userLoginRequestedEvent(jsonNode);
                        break;
                    case "tokenRetrieved":
                        tokenRetrievedEvent(jsonNode);
                        break;
                    default:
                        System.out.println("Unknown event type: " + eventName);
                        break;
                }
            } catch (Exception exception) {
                exception.printStackTrace(System.out);
            }
        };
    }
}
