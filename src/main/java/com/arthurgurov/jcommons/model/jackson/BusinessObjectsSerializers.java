package com.arthurgurov.jcommons.model.jackson;

import com.arthurgurov.jcommons.model.Ref;
import com.arthurgurov.jcommons.model.RefList;
import com.arthurgurov.jcommons.model.RefSet;
import com.arthurgurov.jcommons.model.ValueObject;
import com.arthurgurov.jcommons.model.jackson.component.RefJsonComponent;
import com.arthurgurov.jcommons.model.jackson.component.RefListJsonComponent;
import com.arthurgurov.jcommons.model.jackson.component.RefSetJsonComponent;
import com.arthurgurov.jcommons.model.jackson.component.ValueObjectJsonComponent;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class BusinessObjectsSerializers extends Serializers.Base {

    @Override
    public JsonSerializer<?> findReferenceSerializer(
        final SerializationConfig config,
        final ReferenceType refType,
        final BeanDescription beanDesc,
        final TypeSerializer contentTypeSerializer,
        final JsonSerializer<Object> contentValueSerializer
    ) {
        final Class<?> raw = refType.getRawClass();
        if (Ref.class.isAssignableFrom(raw)) {
            return new RefJsonComponent.Serializer();
        }
        return super.findReferenceSerializer(config, refType, beanDesc, contentTypeSerializer, contentValueSerializer);
    }

    @Override
    public JsonSerializer<?> findSerializer(
        final SerializationConfig config,
        final JavaType type,
        final BeanDescription beanDesc
    ) {
        final Class<?> raw = type.getRawClass();
        if (ValueObject.class.isAssignableFrom(raw)) {
            return new ValueObjectJsonComponent.Serializer();
        }
        return super.findSerializer(config, type, beanDesc);
    }

    @Override
    public JsonSerializer<?> findCollectionSerializer(
        final SerializationConfig config,
        final CollectionType type,
        final BeanDescription beanDesc,
        final TypeSerializer elementTypeSerializer,
        final JsonSerializer<Object> elementValueSerializer
    ) {
        final Class<?> raw = type.getRawClass();
        if (RefList.class.isAssignableFrom(raw)) {
            return new RefListJsonComponent.Serializer(type.getContentType(), false,
                elementTypeSerializer, elementValueSerializer);
        }
        if (RefSet.class.isAssignableFrom(raw)) {
            return new RefSetJsonComponent.Serializer(type.getContentType(), false,
                elementTypeSerializer, elementValueSerializer);
        }
        return super.findCollectionSerializer(config, type, beanDesc, elementTypeSerializer, elementValueSerializer);
    }
}
