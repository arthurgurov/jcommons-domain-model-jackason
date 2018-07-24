package com.arthurgurov.jcommons.model.jackson.component;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.CollectionSerializer;

public class RefSetJsonComponent extends RefCollectionJsonComponent {

    public static class Serializer extends RefCollectionJsonComponent.Serializer {

        public Serializer(final JavaType elemType,
                          final boolean staticTyping,
                          final TypeSerializer vts,
                          final JsonSerializer<Object> valueSerializer) {
            super(elemType, staticTyping, vts, valueSerializer);
        }

        public Serializer(final CollectionSerializer src,
                          final BeanProperty property,
                          final TypeSerializer vts,
                          final JsonSerializer<?> valueSerializer,
                          final Boolean unwrapSingle) {
            super(src, property, vts, valueSerializer, unwrapSingle);
        }
    }
}