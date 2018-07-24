package com.arthurgurov.jcommons.model.jackson.model;

import com.arthurgurov.jcommons.model.vo.StringValueObject;

/**
 * A unique identifier of a user across the entire system.
 */
public final class Username extends StringValueObject<Username> {

    private static final String PATTERN = "^[a-z0-9-.]*$";

    public Username(final String value) {
        super(value);
    }

    private Username(final Username username) {
        super(username);
    }

    @Override
    public Username copy() {
        return new Username(this);
    }
}
