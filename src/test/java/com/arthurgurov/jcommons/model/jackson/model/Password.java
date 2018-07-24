package com.arthurgurov.jcommons.model.jackson.model;

import com.arthurgurov.jcommons.model.vo.StringValueObject;

public final class Password extends StringValueObject<Password> {

    public Password(final String value) {
        super(value);
    }

    private Password(final Password password) {
        super(password);
    }

    @Override
    public String asFormattedString() {
        return "[Password]";
    }

    @Override
    public Password copy() {
        return new Password(this);
    }
}
