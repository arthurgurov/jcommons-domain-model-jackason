package com.arthurgurov.jcommons.model.jackson.model;

import com.arthurgurov.jcommons.model.Entity;

public abstract class UserBase extends Entity {

    private Username username;

    private Password password;

    public Username getUsername() {
        return username;
    }

    public void setUsername(final Username username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(final Password password) {
        this.password = password;
    }
}
