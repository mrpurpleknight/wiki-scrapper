package com.lastminute.recruitment.domain.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WikiInvalidPageTest {

    @Test
    void shouldCreateExceptionWithCorrectMessage() {
        String link = "https://example.com/page";

        WikiInvalidPage exception = new WikiInvalidPage(link);

        assertEquals("Resource is invalid: " + link, exception.getMessage());
        assertTrue(exception.getMessage().contains(link));
    }

    @Test
    void shouldBeRuntimeException() {
        WikiInvalidPage exception = new WikiInvalidPage("test");

        assertTrue(exception instanceof RuntimeException);
    }
}