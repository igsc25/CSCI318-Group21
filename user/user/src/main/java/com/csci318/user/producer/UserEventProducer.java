package com.csci318.user.producer;

import com.csci318.user.model.entity.Admin;
import com.csci318.user.model.entity.Customer;
import com.csci318.user.model.event.AdminEvent;
import com.csci318.user.model.event.CustomerEvent;
import com.csci318.user.model.event.LoginEvent;
import com.csci318.user.model.event.RegistrationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {
    private final StreamBridge streamBridge;

    @Autowired
    public UserEventProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    private AdminEvent toAdminEvent(Admin admin, String eventName) {
        AdminEvent adminEvent = new AdminEvent();

        adminEvent.setEventName(eventName);
        adminEvent.setUserID(admin.getUserID());
        adminEvent.setFirstName(admin.getFirstName());
        adminEvent.setLastName(admin.getLastName());
        adminEvent.setEmail(admin.getEmail());
        adminEvent.setPassword(admin.getPassword());

        return adminEvent;
    }

    private CustomerEvent toCustomerEvent(Customer customer, String eventName) {
        CustomerEvent customerEvent = new CustomerEvent();

        customerEvent.setEventName(eventName);
        customerEvent.setUserID(customer.getUserID());
        customerEvent.setFirstName(customer.getFirstName());
        customerEvent.setLastName(customer.getLastName());
        customerEvent.setEmail(customer.getEmail());
        customerEvent.setPassword(customer.getPassword());
        customerEvent.setPhoneNumber(customer.getPhoneNumber());
        customerEvent.setAddresses(customer.getAddresses());

        return customerEvent;
    }

    public void publishRegistrationFailedEvent(RegistrationEvent registrationEvent) {
        streamBridge.send("user-out-0", registrationEvent);
    }

    public void publishRegistrationSuccessEvent(RegistrationEvent registrationEvent) {
        streamBridge.send("user-out-0", registrationEvent);
    }

    public void publishPasswordRetrievedEvent(LoginEvent loginEvent) {
        streamBridge.send("user-out-0", loginEvent);
    }
}
