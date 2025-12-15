package com.lastminute.recruitment.domain.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WikiPageNotFoundTest {

    @Test
    void shouldCreateExceptionWithCorrectMessage() {
        String link = "https://example.com/page";

        WikiPageNotFound exception = new WikiPageNotFound(link);

        assertEquals("Wiki page not found: " + link, exception.getMessage());
        assertTrue(exception.getMessage().contains(link));
    }

    @Test
    void shouldBeRuntimeException() {
        WikiPageNotFound exception = new WikiPageNotFound("test");

        assertTrue(exception instanceof RuntimeException);
    }
}