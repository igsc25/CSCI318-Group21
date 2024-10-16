package com.csci318.auth.consumer;

import com.csci318.auth.service.TokenSessionService;
import com.csci318.auth.util.PasswordHashing;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UserEventConsumer {
    @Autowired
    private final TokenSessionService tokenSessionService;

    @Autowired
    private final ObjectMapper objectMapper;

    public UserEventConsumer(TokenSessionService tokenSessionService, ObjectMapper objectMapper) {
        this.tokenSessionService = tokenSessionService;
        this.objectMapper = objectMapper;
    }

    private void RegistrationFailedEvent(JsonNode jsonNode) {
        try {
            String email = jsonNode.get("email").asText();
            System.out.println("Email: " + email + "already existed!");
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    private void RegistrationSucceedEvent(JsonNode jsonNode) {
        try {
            String email = jsonNode.get("email").asText();
            System.out.println("Email: " + email + "successfully registered!");
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    private void passwordRetrievedEvent(JsonNode jsonNode) {
        try {
            String email = jsonNode.get("email").asText();
            String inputPassword = jsonNode.get("inputPassword").asText();
            String databasePassword = jsonNode.get("databasePassword").asText();
            String passwordSalt = jsonNode.get("passwordSalt").asText();
            String role = jsonNode.get("role").asText();

            boolean passwordMatches = PasswordHashing.checkPassword(inputPassword, databasePassword, passwordSalt);
            System.out.print("Password matching result: " + passwordMatches);

            tokenSessionService.completeLogin(email, passwordMatches, role);
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    @Bean
    public Consumer<String> userEventListener() {
        return message -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(message);

                String eventName = jsonNode.get("eventName").asText();

                switch (eventName) {
                    case "registrationFailed":
                        RegistrationFailedEvent(jsonNode);
                        break;
                    case "registrationSucceed":
                        RegistrationSucceedEvent(jsonNode);
                        break;
                    case "passwordRetrieved":
                        passwordRetrievedEvent(jsonNode);
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
