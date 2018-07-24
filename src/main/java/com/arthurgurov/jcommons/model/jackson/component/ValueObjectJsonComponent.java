package com.arthurgurov.jcommons.model.jackson.component;

import com.arthurgurov.jcommons.model.ValueObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ValueObjectJsonComponent {

    public static class Serializer extends JsonSerializer<ValueObject> {

        @Override
        public void serialize(
            final ValueObject value,
            final JsonGenerator gen,
            final SerializerProvider serializers
        ) throws IOException {
            gen.writeString(value.asString());
        }
    }
}