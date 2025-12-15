package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.rest.error.WikiInvalidLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WikiLinkValidatorTest {
    private WikiLinkValidator validator;

    @BeforeEach
    void setUp() {
        validator = new WikiLinkValidator();
    }

    @Test
    void validatePasses() {
        assertDoesNotThrow(() -> validator.validate("https://wikiscrapper.test/site123"));
        assertDoesNotThrow(() -> validator.validate("http://wikiscrapper.test/site1"));
    }

    @Test
    void validateThrowsForInvalidScheme() {
        WikiInvalidLink ex = assertThrows(WikiInvalidLink.class,
                () -> validator.validate("ftp://wikiscrapper.test/site123"));
        assertTrue(ex.getMessage().contains("Invalid URI scheme"));
    }

    @Test
    void validateThrowsForInvalidHost() {
        WikiInvalidLink ex = assertThrows(WikiInvalidLink.class,
                () -> validator.validate("https://example.com/site123"));
        assertTrue(ex.getMessage().contains("Invalid URI host"));
    }

    @Test
    void validateThrowsForMissingSchemeOrHost() {
        WikiInvalidLink ex1 = assertThrows(WikiInvalidLink.class,
                () -> validator.validate("wikiscrapper.test/site123"));
        assertTrue(ex1.getMessage().contains("URI must include scheme and host"));

        WikiInvalidLink ex2 = assertThrows(WikiInvalidLink.class,
                () -> validator.validate("https:///site123"));
        assertTrue(ex2.getMessage().contains("URI must include scheme and host"));
    }

    @Test
    void validateThrowsForInvalidPath() {
        WikiInvalidLink ex1 = assertThrows(WikiInvalidLink.class,
                () -> validator.validate("https://wikiscrapper.test"));
        assertTrue(ex1.getMessage().contains("URI path is missing"));

        WikiInvalidLink ex2 = assertThrows(WikiInvalidLink.class,
                () -> validator.validate("https://wikiscrapper.test/"));
        assertTrue(ex2.getMessage().contains("URI path is missing"));

        WikiInvalidLink ex3 = assertThrows(WikiInvalidLink.class,
                () -> validator.validate("https://wikiscrapper.test/site123/extra"));
        assertTrue(ex3.getMessage().contains("Invalid URI path"));
    }

    @Test
    void validateThrowsForInvalidPageName() {
        WikiInvalidLink ex = assertThrows(WikiInvalidLink.class,
                () -> validator.validate("https://wikiscrapper.test/invalidPage"));
        assertTrue(ex.getMessage().contains("Invalid page name"));
    }

    @Test
    void validateThrowsForInvalidFormat() {
        WikiInvalidLink ex = assertThrows(WikiInvalidLink.class,
                () -> validator.validate("http://wikiscrapper.test/%%%"));
        assertTrue(ex.getMessage().contains("Invalid URI format"));
    }
}
