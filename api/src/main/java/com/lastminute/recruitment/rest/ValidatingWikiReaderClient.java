package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.domain.client.WikiReaderClient;
import com.lastminute.recruitment.domain.model.WikiPage;

public final class ValidatingWikiReaderClient implements WikiReaderClient {
    private final WikiReaderClient delegate;
    private final WikiLinkValidator validator;

    public ValidatingWikiReaderClient(WikiReaderClient delegate, WikiLinkValidator validator) {
        this.delegate = delegate;
        this.validator = validator;
    }

    @Override
    public WikiPage read(String link) {
        validator.validate(link);
        return delegate.read(link);
    }
}
