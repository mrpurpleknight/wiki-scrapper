package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.domain.client.WikiReaderClient;
import com.lastminute.recruitment.domain.model.WikiPage;
import com.lastminute.recruitment.rest.error.WikiInvalidLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidatingLinkReaderClientTest {
    private WikiReaderClient delegate;
    private WikiLinkValidator validator;
    private ValidatingWikiReaderClient validatingClient;

    @BeforeEach
    void setUp() {
        delegate = mock(WikiReaderClient.class);
        validator = mock(WikiLinkValidator.class);
        validatingClient = new ValidatingWikiReaderClient(delegate, validator);
    }

    @Test
    void readValidatesAndDelegates() {
        String link = "https://wikiscrapper.test/site123";
        WikiPage page = mock(WikiPage.class);
        when(delegate.read(link)).thenReturn(page);

        WikiPage result = validatingClient.read(link);

        verify(validator).validate(link);
        verify(delegate).read(link);
        assertSame(page, result);
    }

    @Test
    void readValidatesAndThrowsForInvalidLink() {
        String link = "invalid-link";
        doThrow(new WikiInvalidLink("Invalid link")).when(validator).validate(link);

        assertThrows(WikiInvalidLink.class, () -> validatingClient.read(link));
        verify(validator).validate(link);
        verify(delegate, never()).read(any());
    }
}
