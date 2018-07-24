package com.arthurgurov.jcommons.model.jackson.component;

import com.arthurgurov.jcommons.model.BusinessObject;
import com.arthurgurov.jcommons.model.Entity;
import com.arthurgurov.jcommons.model.Ref;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;

public class RefJsonComponent {

    public static class Serializer extends JsonSerializer<Ref> {

        @Override
        public void serialize(
            final Ref ref,
            final JsonGenerator gen,
            final SerializerProvider serializers
        ) throws IOException {

            if (!ref.hasValue()) {
                serializers.defaultSerializeNull(gen);
                return;
            }

            final Object value = ref.getValue();
            serializers.defaultSerializeValue(value instanceof Entity ?
                ((Entity) value).getIdentity() : value, gen);
        }

        @Override
        public boolean isEmpty(final SerializerProvider provider, final Ref value) {
            return value == null || !value.hasValue();
        }
    }

    public static class Deserializer extends StdDeserializer<Ref<?>>
        implements ContextualDeserializer {

        private static final long serialVersionUID = 1L;

        protected final JavaType _fullType;

        protected final JsonDeserializer<?> _valueDeserializer;

        protected final TypeDeserializer _valueTypeDeserializer;

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

        public Deserializer(
            final JavaType fullType,
            final TypeDeserializer typeDeser,
            final JsonDeserializer<?> valueDeser
        ) {
            super(fullType);
            _fullType = fullType;
            _valueTypeDeserializer = typeDeser;
            _valueDeserializer = valueDeser;
        }

        /**
         * Overridable fluent factory method used for creating contextual instances.
         */
        protected Deserializer withResolved(final TypeDeserializer typeDeser, final JsonDeserializer<?> valueDeser) {
            if ((valueDeser == _valueDeserializer) && (typeDeser == _valueTypeDeserializer)) {
                return this;
            }
            return new Deserializer(_fullType, typeDeser, valueDeser);
        }

        /**
         * Method called to finalize setup of this deserializer, after deserializer itself has been
         * registered. This is needed to handle recursive and transitive dependencies.
         */
        @Override
        public JsonDeserializer<?> createContextual(
            final DeserializationContext ctxt,
            final BeanProperty property
        ) throws JsonMappingException {

            JsonDeserializer<?> deser = _valueDeserializer;
            TypeDeserializer typeDeser = _valueTypeDeserializer;
            final JavaType refType = _fullType.getReferencedType();

            if (deser == null) {
                deser = ctxt.findContextualValueDeserializer(refType, property);
            } else { // otherwise directly assigned, probably not contextual yet:
                deser = ctxt.handleSecondaryContextualization(deser, property, refType);
            }
            if (typeDeser != null) {
                typeDeser = typeDeser.forProperty(property);
            }
            return withResolved(typeDeser, deser);
        }

    /*
    /**********************************************************
    /* Overridden accessors
    /**********************************************************
     */

        @Override
        public JavaType getValueType() {
            return _fullType;
        }

        @Override
        public Ref<?> getNullValue(final DeserializationContext ctxt) {
            return Ref.optional();  // new Ref();
        }

    /*
    /**********************************************************
    /* Deserialization
    /**********************************************************
     */

        @Override
        public Ref<?> deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {

            final Object refd = (_valueTypeDeserializer == null)
                ? _valueDeserializer.deserialize(p, ctxt)
                : _valueDeserializer.deserializeWithType(p, ctxt, _valueTypeDeserializer);

            final Ref<BusinessObject> ref = Ref.optional();
            ref.setValue((BusinessObject) refd);

            return ref;// new Ref((BusinessObject) refd);
        }

        @Override
        public Ref<?> deserializeWithType(
            final JsonParser p,
            final DeserializationContext ctxt,
            final TypeDeserializer typeDeserializer) throws IOException {

            final JsonToken t = p.getCurrentToken();
            if (t == JsonToken.VALUE_NULL) {
                return getNullValue(ctxt);
            }
            // 03-Nov-2013, tatu: This gets rather tricky with "natural" types
            //   (String, Integer, Boolean), which do NOT include type information.
            //   These might actually be handled ok except that nominal type here
            //   is `Optional`, so special handling is not invoked; instead, need
            //   to do a work-around here.
            // 22-Oct-2015, tatu: Most likely this is actually wrong, result of incorrect
            //   serialization (up to 2.6, was omitting necessary type info after all);
            //   but safest to leave in place for now
            if (t != null && t.isScalarValue()) {
                return deserialize(p, ctxt);
            }
            return (Ref<?>) typeDeserializer.deserializeTypedFromAny(p, ctxt);
        }
    }
}