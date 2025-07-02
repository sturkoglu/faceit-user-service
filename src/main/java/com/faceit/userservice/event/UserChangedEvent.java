package com.faceit.userservice.event;

import com.faceit.userservice.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


public class UserChangedEvent extends ApplicationEvent {

    @Getter
    private final String action;
    @Getter
    private final User user;

    public UserChangedEvent(Object source, UserChangedEventAction action, User user) {
        super(source);
        this.action = action.toString();
        this.user = user;
    }

}
