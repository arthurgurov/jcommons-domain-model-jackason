package com.arthurgurov.jcommons.model.jackson;

import com.arthurgurov.jcommons.model.Ref;
import com.arthurgurov.jcommons.model.jackson.component.RefJsonComponent;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class BusinessObjectsDeserializers extends Deserializers.Base {

    @Override // since 2.7
    public JsonDeserializer<?> findReferenceDeserializer(
        final ReferenceType refType,
        final DeserializationConfig config,
        final BeanDescription beanDesc,
        final TypeDeserializer contentTypeDeserializer,
        final JsonDeserializer<?> contentDeserializer
    ) {
        if (refType.hasRawClass(Ref.class)) {
            return new RefJsonComponent.Deserializer(refType, contentTypeDeserializer, contentDeserializer);
        }
        return null;
    }
}