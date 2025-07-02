package com.faceit.userservice.event;

import com.faceit.userservice.entity.User;
import org.springframework.context.ApplicationEvent;

public class UserChangedEvent extends ApplicationEvent {

    private final String action; // e.g., "CREATED", "UPDATED", "DELETED"
    private final User user;

    public UserChangedEvent(Object source, String action, User user) {
        super(source);
        this.action = action;
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public User getUser() {
        return user;
    }
}
