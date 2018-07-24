package com.arthurgurov.jcommons.model.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class EntityObjectMapper extends ObjectMapper {

    public EntityObjectMapper() {
        // Deserialization features
        enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Serialization features
        enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        disable(SerializationFeature.WRAP_ROOT_VALUE);

        // Serialization inclusion
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        // Visibility
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);

        setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.ANY);

        registerModule(new BusinessObjectsModule());
    }
}