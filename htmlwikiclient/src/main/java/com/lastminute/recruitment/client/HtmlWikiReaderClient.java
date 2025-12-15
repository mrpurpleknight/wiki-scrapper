package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.error.WikiInvalidPage;
import com.lastminute.recruitment.domain.model.WikiPage;
import com.lastminute.recruitment.domain.client.WikiReaderClient;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

public class HtmlWikiReaderClient implements WikiReaderClient {
    private static final String DOMAIN = "http://wikiscrapper.test";
    private static final String BASE_PATH = "wikiscrapper";

    private final ClassLoader classLoader = this.getClass().getClassLoader();

    @Override
    public WikiPage read(String link) {
        String resourcePath = resolveResourcePath(link, BASE_PATH, "html");
        try (InputStream pageStream = classLoader.getResourceAsStream(resourcePath)) {
            if (pageStream == null) {
                throw new WikiPageNotFound(link);
            }

            Document document = Jsoup.parse(pageStream, StandardCharsets.UTF_8.name(), DOMAIN);
            String title = textContent(document, "h1.title");
            String content = textContent(document, "p.content");
            String selfLink = selfLink(document);
            List<String> links = streamElements(document, "ul.links a")
                    .map(element -> element.attr("href"))
                    .toList();

            return new WikiPage(title, content, selfLink, links);
        } catch (IOException e) {
            throw new WikiInvalidPage(link);
        }
    }

    private String textContent(Document document, String selector) {
        Element element = document.selectFirst(selector);
        return element != null ? element.text() : "";
    }

    private String selfLink(Document document) {
        Element element = document.selectFirst("meta[selfLink]");
        if (element == null) {
            throw new WikiInvalidPage("Missing meta tag with selfLink attribute");
        }

        String selfLink = element.attr("selfLink");
        if (selfLink.isBlank()) {
            throw new WikiInvalidPage("Empty selfLink attribute in meta tag");
        }

        return selfLink;
    }

    private Stream<Element> streamElements(Document document, String selector) {
        return document.select(selector).stream();
    }
}
