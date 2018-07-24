package com.arthurgurov.jcommons.model.jackson.component;

import com.arthurgurov.jcommons.model.Entity;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.CollectionSerializer;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class RefCollectionJsonComponent {

    public static class Serializer extends CollectionSerializer {

        public Serializer(JavaType elemType, boolean staticTyping, TypeSerializer vts,
                          JsonSerializer<Object> valueSerializer) {
            super(elemType, staticTyping, vts, valueSerializer);
        }

        public Serializer(CollectionSerializer src,
                          BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer,
                          Boolean unwrapSingle) {
            super(src, property, vts, valueSerializer, unwrapSingle);
        }

        private static Object substituteValue(final Object value) {
            return value instanceof Entity ? ((Entity) value).getIdentity() : value;
        }

        private static void serialize(final Object elem, final JsonGenerator jgen, final SerializerProvider provider,
                                      final TypeSerializer typeSer, JsonSerializer<Object> ser) throws IOException {
            if (typeSer == null) {
                ser.serialize(elem, jgen, provider);
            } else {
                ser.serializeWithType(elem, jgen, provider, typeSer);
            }
        }

        @Override
        public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new Serializer(this, _property, vts, _elementSerializer, _unwrapSingle);
        }

        @Override
        public Serializer withResolved(BeanProperty property,
                                       TypeSerializer vts, JsonSerializer<?> elementSerializer,
                                       Boolean unwrapSingle) {
            return new Serializer(this, property, vts, elementSerializer, unwrapSingle);
        }

        @Override
        public void serializeContents(Collection<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (_elementSerializer != null) {
                serializeContentsUsing(value, jgen, provider, _elementSerializer);
                return;
            }
            Iterator<?> it = value.iterator();
            if (!it.hasNext()) {
                return;
            }
            PropertySerializerMap serializers = _dynamicSerializers;

            int i = 0;
            try {
                do {
                    Object elem = substituteValue(it.next());
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    } else {
                        Class<?> cc = elem.getClass();
                        JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                        if (serializer == null) {
                            if (_elementType.hasGenericTypes()) {
                                serializer = _findAndAddDynamic(serializers,
                                    provider.constructSpecializedType(_elementType, cc), provider);
                            } else {
                                serializer = _findAndAddDynamic(serializers, cc, provider);
                            }
                            serializers = _dynamicSerializers;
                        }
                        serialize(elem, jgen, provider, _valueTypeSerializer, serializer);
                    }
                    ++i;
                } while (it.hasNext());
            } catch (Exception e) {
                wrapAndThrow(provider, e, value, i);
            }
        }

        @Override
        public void serializeContentsUsing(Collection<?> value, JsonGenerator jgen, SerializerProvider provider,
                                           JsonSerializer<Object> ser)
            throws IOException, JsonGenerationException {

            Iterator<?> it = value.iterator();
            if (it.hasNext()) {
                int i = 0;
                do {
                    Object elem = substituteValue(it.next());
                    try {
                        if (elem == null) {
                            provider.defaultSerializeNull(jgen);
                        } else {
                            serialize(elem, jgen, provider, _valueTypeSerializer, ser);
                        }
                        ++i;
                    } catch (Exception e) {
                        wrapAndThrow(provider, e, value, i);
                    }
                } while (it.hasNext());
            }
        }
    }
}