package com.arthurgurov.jcommons.model.jackson.model;

public class User extends UserBase {

    public User() {
        // empty
    }

    public User(final String username, final String password) {
        this(new Username(username), new Password(password));
    }

    public User(final Username username, final Password password) {
        setUsername(username);
        setPassword(password);
    }

    @Override
    public Object getIdentity() {
        return getUsername();
    }
}
