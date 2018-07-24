package com.arthurgurov.jcommons.model.jackson;

import com.arthurgurov.jcommons.model.Ref;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;

import java.lang.reflect.Type;

public class BusinessObjectsTypeModifier extends TypeModifier {

    @Override
    public JavaType modifyType(
        final JavaType type,
        final Type jdkType,
        final TypeBindings bindings,
        final TypeFactory typeFactory
    ) {
        if (type.isReferenceType() || type.isContainerType()) {
            return type;
        }
        final Class<?> raw = type.getRawClass();

        final JavaType refType;

        if (raw == Ref.class) {
            refType = type.containedTypeOrUnknown(0);
        } else {
            return type;
        }
        return ReferenceType.upgradeFrom(type, refType);
    }
}