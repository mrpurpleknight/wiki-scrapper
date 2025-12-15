package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.client.WikiReaderClient;
import com.lastminute.recruitment.domain.model.WikiPage;
import com.lastminute.recruitment.domain.repository.WikiPageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WikiScrapperTest {
    private WikiReaderClient reader;
    private WikiPageRepository repository;
    private WikiScrapper scrapper;

    @BeforeEach
    void setUp() {
        reader = mock(WikiReaderClient.class);
        repository = mock(WikiPageRepository.class);
        scrapper = new WikiScrapper(reader, repository);
    }

    @Test
    void readSavesSinglePageWithNoLinks() {
        WikiPage page = new WikiPage("Root", "Content Root", "/root", List.of());
        when(reader.read("/root")).thenReturn(page);
        List<WikiPage> result = scrapper.read("/root");
        verify(repository).save(page);
        verifyNoMoreInteractions(repository);
        assertEquals(1, result.size());
        assertEquals("/root", result.get(0).selfLink());
    }

    @Test
    void readSaveMultiplePages() {
        WikiPage page1 = new WikiPage("Root", "Content Root", "/root", List.of("/a", "/b"));
        WikiPage pageA = new WikiPage("Test A", "Content A", "/a", List.of("/b", "/root")); // cycle
        WikiPage pageB = new WikiPage("Test B", "Content B", "/b", List.of("/c"));
        WikiPage pageC = new WikiPage("Test C", "Content C", "/c", List.of());
        when(reader.read("/root")).thenReturn(page1);
        when(reader.read("/a")).thenReturn(pageA);
        when(reader.read("/b")).thenReturn(pageB);
        when(reader.read("/c")).thenReturn(pageC);
        List<WikiPage> result = scrapper.read("/root");
        ArgumentCaptor<WikiPage> captor = ArgumentCaptor.forClass(WikiPage.class);
        verify(repository, atLeast(1)).save(captor.capture());
        Set<String> savedLinks = captor.getAllValues().stream().map(WikiPage::selfLink).collect(Collectors.toSet());
        assertEquals(Set.of("/root", "/a", "/b", "/c"), savedLinks);
        Set<String> resultLinks = result.stream().map(WikiPage::selfLink).collect(Collectors.toSet());
        assertEquals(Set.of("/root", "/a", "/b", "/c"), resultLinks);
    }

    @Test
    void readIgnoresNullAndBlankLinks() {
        List<String> links = new java.util.ArrayList<>();
        links.add(null);
        links.add("");
        links.add(" ");
        links.add("/a");
        WikiPage page = new WikiPage("Root", "Content Root", "/root", links);
        WikiPage pageA = new WikiPage("Test A", "Content A", "/a", List.of());
        when(reader.read("/root")).thenReturn(page);
        when(reader.read("/a")).thenReturn(pageA);
        List<WikiPage> result = scrapper.read("/root");
        verify(repository).save(page);
        verify(repository).save(pageA);
        verifyNoMoreInteractions(repository);
        assertEquals(2, result.size());
        Set<String> resultLinks = result.stream().map(WikiPage::selfLink).collect(Collectors.toSet());
        assertEquals(Set.of("/root", "/a"), resultLinks);
    }

    @Test
    void readHandlesDuplicatesAndCycles() {
        WikiPage page = new WikiPage("Root", "Content Root", "/root", List.of("/a", "/b", "/root"));
        WikiPage pageA = new WikiPage("Test A", "Content A", "/a", List.of("/root"));
        WikiPage pageB = new WikiPage("Test B", "Content B", "/b", List.of("/a", "/root"));

        when(reader.read("/root")).thenReturn(page);
        when(reader.read("/a")).thenReturn(pageA);
        when(reader.read("/b")).thenReturn(pageB);

        List<WikiPage> result = scrapper.read("/root");

        verify(repository).save(page);
        verify(repository).save(pageA);
        verify(repository).save(pageB);
        verifyNoMoreInteractions(repository);
        assertEquals(3, result.size());
        Set<String> resultLinks = result.stream().map(WikiPage::selfLink).collect(Collectors.toSet());
        assertEquals(Set.of("/root", "/a", "/b"), resultLinks);
    }

    @Test
    void readHandlesNullLinksList() {
        WikiPage page = new WikiPage("Root", "Content Root", "/root", null);
        when(reader.read("/root")).thenReturn(page);
        List<WikiPage> result = scrapper.read("/root");
        verify(repository).save(page);
        verifyNoMoreInteractions(repository);
        assertEquals(1, result.size());
        assertEquals("/root", result.get(0).selfLink());
    }
}
