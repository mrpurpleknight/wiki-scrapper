package com.lastminute.recruitment.rest.error;

public class WikiInvalidLink extends RuntimeException {

    public WikiInvalidLink(String reason) {
        super(reason);
    }
}
