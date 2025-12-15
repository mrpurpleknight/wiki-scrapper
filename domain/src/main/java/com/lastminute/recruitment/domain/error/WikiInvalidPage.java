package com.lastminute.recruitment.domain.error;

public class WikiInvalidPage extends RuntimeException {

    public WikiInvalidPage(String resource) {
        super("Resource is invalid: " + resource);
    }
}
