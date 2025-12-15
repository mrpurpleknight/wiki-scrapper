package com.lastminute.recruitment.rest.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WikiInvalidLinkTest {

    @Test
    void shouldCreateExceptionWithCorrectMessage() {
        String reason = "Test reason";

        WikiInvalidLink exception = new WikiInvalidLink(reason);

        assertEquals(reason, exception.getMessage());
    }

    @Test
    void shouldBeRuntimeException() {
        WikiInvalidLink exception = new WikiInvalidLink("test");

        assertTrue(exception instanceof RuntimeException);
    }
}