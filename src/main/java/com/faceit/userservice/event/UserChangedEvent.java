package com.faceit.userservice.event;

import com.faceit.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChangedEvent{

    private UserChangedEventAction action;
    private User user;

    public UserChangedEvent(Object source, UserChangedEventAction action, User user) {
        this.action = action;
        this.user = user;
    }

}
