package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.client.WikiReaderClient;
import com.lastminute.recruitment.domain.model.WikiPage;
import com.lastminute.recruitment.domain.repository.WikiPageRepository;

import java.util.*;

public class WikiScrapper {

    private final WikiReaderClient wikiReaderClient;
    private final WikiPageRepository wikiPageRepository;

    public WikiScrapper(WikiReaderClient wikiReaderClient, WikiPageRepository wikiPageRepository) {
        this.wikiReaderClient = wikiReaderClient;
        this.wikiPageRepository = wikiPageRepository;
    }

    public List<WikiPage> read(String link) {
        WikiPage rootPage = wikiReaderClient.read(link);

        Map<String, WikiPage> visited = new LinkedHashMap<>();
        Deque<String> toVisit = new ArrayDeque<>();

        processPage(rootPage, visited, toVisit);

        while (!toVisit.isEmpty()) {
            String linkToVisit = toVisit.removeFirst();
            if (visited.containsKey(linkToVisit)) {
                continue;
            }

            WikiPage page = wikiReaderClient.read(linkToVisit);
            if (page != null) {
                processPage(page, visited, toVisit);
            }
        }

        return new ArrayList<>(visited.values());
    }

    private void processPage(WikiPage page, Map<String, WikiPage> visited, Deque<String> toVisit) {
        String selfLink = page.selfLink();
        if (visited.containsKey(selfLink)) {
            return;
        }

        visited.put(selfLink, page);
        wikiPageRepository.save(page);

        if (page.links() != null) {
            page.links().stream()
                    .filter(Objects::nonNull)
                    .filter(link -> !link.isBlank())
                    .filter(link -> !visited.containsKey(link))
                    .forEach(toVisit::addLast);
        }
    }
}
