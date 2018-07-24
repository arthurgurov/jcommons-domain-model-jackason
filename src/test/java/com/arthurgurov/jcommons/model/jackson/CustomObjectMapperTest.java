package com.arthurgurov.jcommons.model.jackson;

import com.arthurgurov.jcommons.model.jackson.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomObjectMapperTest {

    private EntityObjectMapper testInstance;

    @Before
    public void setUp() {
        testInstance = new EntityObjectMapper();
    }

    @Test
    public void test() throws JsonProcessingException {
        assertEquals("{\"username\":\"user\",\"password\":\"pass\"}",
            testInstance.writeValueAsString(new User("user", "pass")));
    }
}
