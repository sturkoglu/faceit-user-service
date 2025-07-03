package com.faceit.userservice.event;

import com.faceit.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;


@Data
@NoArgsConstructor // <-- This is the key!
@AllArgsConstructor
public class UserChangedEvent{

    private UserChangedEventAction action;
    private User user;

    public UserChangedEvent(Object source, UserChangedEventAction action, User user) {
        this.action = action;
        this.user = user;
    }

}
