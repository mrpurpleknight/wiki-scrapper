package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.error.WikiInvalidPage;
import com.lastminute.recruitment.domain.model.WikiPage;
import com.lastminute.recruitment.domain.client.WikiReaderClient;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonWikiClientClient implements WikiReaderClient {
    private static final String BASE_PATH = "wikiscrapper";

    private final ObjectMapper objectMapper;
    private final ClassLoader classLoader = this.getClass().getClassLoader();

    public JsonWikiClientClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public WikiPage read(String link) {
        String resourcePath = resolveResourcePath(link, BASE_PATH, "json");
        try (InputStream pageStream = classLoader.getResourceAsStream(resourcePath)) {
            if (pageStream == null) {
                throw new WikiPageNotFound(link);
            }

            return objectMapper.readValue(pageStream, WikiPage.class);
        } catch (IOException e) {
            throw new WikiInvalidPage(link);
        }
    }
}
