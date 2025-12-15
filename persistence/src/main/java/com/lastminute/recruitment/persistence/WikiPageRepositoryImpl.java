package com.lastminute.recruitment.persistence;

import com.lastminute.recruitment.domain.model.WikiPage;
import com.lastminute.recruitment.domain.repository.WikiPageRepository;

public class WikiPageRepositoryImpl implements WikiPageRepository {
    public void save(WikiPage wikiPage) {
        System.out.println("Saving wiki page: " + wikiPage.toString());
    }
}
