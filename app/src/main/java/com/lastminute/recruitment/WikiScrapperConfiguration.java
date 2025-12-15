package com.lastminute.recruitment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastminute.recruitment.client.HtmlWikiReaderClient;
import com.lastminute.recruitment.client.JsonWikiReaderClient;
import com.lastminute.recruitment.domain.client.WikiReaderClient;
import com.lastminute.recruitment.domain.repository.WikiPageRepository;
import com.lastminute.recruitment.domain.WikiScrapper;
import com.lastminute.recruitment.persistence.WikiPageRepositoryImpl;
import com.lastminute.recruitment.rest.ValidatingWikiReaderClient;
import com.lastminute.recruitment.rest.WikiLinkValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class WikiScrapperConfiguration {

    @Bean
    public WikiLinkValidator wikiLinkValidator() {
        return new WikiLinkValidator();
    }

    @Bean
    @Profile("json")
    @Qualifier("baseClient")
    public WikiReaderClient jsonWikiReader(ObjectMapper objectMapper) {
        return new JsonWikiReaderClient(objectMapper);
    }

    @Bean
    @Profile("html")
    @Qualifier("baseClient")
    public WikiReaderClient htmlWikiReader() {
        return new HtmlWikiReaderClient();
    }

    @Bean
    @Primary
    public WikiReaderClient validatingClient(@Qualifier("baseClient") WikiReaderClient raw,
                                             WikiLinkValidator validator) {
        return new ValidatingWikiReaderClient(raw, validator);
    }

    @Bean
    public WikiPageRepository wikiPageRepository() {
        return new WikiPageRepositoryImpl();
    }

    @Bean
    public WikiScrapper wikiScrapper(WikiReaderClient wikiReaderClient, WikiPageRepository wikiPageRepository) {
        return new WikiScrapper(wikiReaderClient, wikiPageRepository);
    }
}
