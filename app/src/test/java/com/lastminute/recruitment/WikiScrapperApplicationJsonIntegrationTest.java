package com.lastminute.recruitment;

import com.lastminute.recruitment.domain.model.WikiPage;
import com.lastminute.recruitment.domain.repository.WikiPageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("json")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class WikiScrapperApplicationJsonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WikiPageRepository wikiPageRepository;

    @Test
    void scrapesPagesWithJsonProfile() throws Exception {
        mockMvc.perform(post("/wiki/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"http://wikiscrapper.test/site2\""))
                .andExpect(status().isOk());

        ArgumentCaptor<WikiPage> savedPages = ArgumentCaptor.forClass(WikiPage.class);
        verify(wikiPageRepository, atLeastOnce()).save(savedPages.capture());

        List<String> savedTitles = savedPages.getAllValues().stream()
                .map(WikiPage::title)
                .toList();

        assertThat(savedTitles)
                .containsExactlyInAnyOrder(
                        "Site 1",
                        "Site 2",
                        "Site 3",
                        "Site 4",
                        "Site 5"
                );
    }

    @Test
    void returnsNotFoundForMissingPage() throws Exception {
        mockMvc.perform(post("/wiki/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"http://wikiscrapper.test/site123\""))
                .andExpect(status().isNotFound());

        verify(wikiPageRepository, never()).save(any());
    }

    @Test
    void returnsInternalServerErrorForInvalidPage() throws Exception {
        mockMvc.perform(post("/wiki/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"http://wikiscrapper.test/site6\""))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void returnsBadRequestForInvalidLink() throws Exception {
        mockMvc.perform(post("/wiki/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"http://wikiscrapper.test/badlink\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsBadRequestForMalformedRequest() throws Exception {
        mockMvc.perform(post("/wiki/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"{not-a-json-string\""))
                .andExpect(status().isBadRequest());
    }
}
