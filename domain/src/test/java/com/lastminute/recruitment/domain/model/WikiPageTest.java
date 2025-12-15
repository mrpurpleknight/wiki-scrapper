package com.lastminute.recruitment.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WikiPageTest {
    @Test
    void testFields() {
        String title = "Sample Title";
        String content = "Sample Content";
        String selfLink = "/wiki/sample";
        List<String> links = List.of("/wiki/1", "/wiki/2");
        WikiPage page = new WikiPage(title, content, selfLink, links);

        assertEquals(title, page.title());
        assertEquals(content, page.content());
        assertEquals(selfLink, page.selfLink());
        assertEquals(links, page.links());
    }

    @Test
    void testEqualsAndHashCode() {
        WikiPage page1 = new WikiPage("A", "B", "C", List.of("D"));
        WikiPage page2 = new WikiPage("A", "B", "C", List.of("D"));
        WikiPage page3 = new WikiPage("X", "Y", "Z", List.of("Q"));

        assertEquals(page1, page2);
        assertEquals(page1.hashCode(), page2.hashCode());
        assertNotEquals(page1, page3);
        assertNotEquals(page1.hashCode(), page3.hashCode());
    }


    @Test
    void testEmptyLinks() {
        WikiPage page = new WikiPage("T", "C", "L", null);

        assertNull(page.links());
    }

    @Test
    void testSelfLinkValidation() {
        assertThrows(IllegalArgumentException.class, () ->
                new WikiPage("T", "C", null, List.of())
        );
        assertThrows(IllegalArgumentException.class, () ->
                new WikiPage("T", "C", "", List.of())
        );
        assertThrows(IllegalArgumentException.class, () ->
                new WikiPage("T", "C", "   ", List.of())
        );
    }
}
