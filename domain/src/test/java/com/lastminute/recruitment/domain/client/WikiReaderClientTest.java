package com.lastminute.recruitment.domain.client;

import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WikiReaderClientTest {

    @Test
    void resolveResourcePathReturnsCorrectPath() {
        WikiReaderClient client = link -> null;
        String link = "https://example.com/page";
        String basePath = "base";
        String extension = "html";
        String expected = "base/page.html";
        String result = client.resolveResourcePath(link, basePath, extension);
        assertEquals(expected, result);
    }

    @Test
    void resolveResourcePathThrowsForInvalidLink() {
        WikiReaderClient client = link -> null;
        assertThrows(WikiPageNotFound.class, () ->
                client.resolveResourcePath("https://??example.com/", "base", "html")
        );
    }
}
