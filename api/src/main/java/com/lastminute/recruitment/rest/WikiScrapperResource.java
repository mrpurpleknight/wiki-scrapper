package com.lastminute.recruitment.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastminute.recruitment.domain.WikiScrapper;
import com.lastminute.recruitment.domain.model.WikiPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/wiki")
@RestController
public class WikiScrapperResource {

    private final ObjectMapper objectMapper;
    private final WikiScrapper wikiScrapper;

    public WikiScrapperResource(ObjectMapper objectMapper, WikiScrapper wikiScrapper) {
        this.objectMapper = objectMapper;
        this.wikiScrapper = wikiScrapper;
    }

    @PostMapping(value = "/scrap", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> scrapWikipedia(@RequestBody String link) throws JsonProcessingException {
        /// Path Traversal vulnerability is managed in [ValidatingWikiReaderClient]
        String decodedLink = objectMapper.readValue(link, String.class);
        List<WikiPage> pages = wikiScrapper.read(decodedLink);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(pages.stream()
                        .map(WikiPage::selfLink)
                        .reduce((a, b) -> a + "\n" + b)
                        .orElse(""));

    }

}
