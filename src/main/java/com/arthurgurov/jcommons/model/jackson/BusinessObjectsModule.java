package com.arthurgurov.jcommons.model.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class BusinessObjectsModule extends SimpleModule {

    public BusinessObjectsModule() {
        super(BusinessObjectsModule.class.getSimpleName(),
            new Version(0, 1, 0, null, null, null));
    }

    @Override
    public void setupModule(final SetupContext context) {
        context.addSerializers(new BusinessObjectsSerializers());
        context.addDeserializers(new BusinessObjectsDeserializers());

        // And to fully support Ref, need to modify type info:
        context.addTypeModifier(new BusinessObjectsTypeModifier());
    }
}