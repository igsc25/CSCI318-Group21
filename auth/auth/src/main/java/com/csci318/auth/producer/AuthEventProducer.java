package com.csci318.auth.producer;

import com.csci318.auth.model.event.LoginEvent;
import com.csci318.auth.model.event.RegistrationEvent;
import com.csci318.auth.model.event.TokenRetrievedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class AuthEventProducer {
    private final StreamBridge streamBridge;

    @Autowired
    public AuthEventProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishRegistrationEvent(RegistrationEvent registrationEvent) {
        System.out.println("Registration event sent");
        streamBridge.send("auth-out-0", registrationEvent);
    }

    public void publishLoginEvent(LoginEvent loginEvent) {
        streamBridge.send("auth-out-0", loginEvent);
    }

    public void publishTokenRetrievedEvent(TokenRetrievedEvent tokenRetrievedEvent) {
        streamBridge.send("auth-out-0", tokenRetrievedEvent);
    }
}
