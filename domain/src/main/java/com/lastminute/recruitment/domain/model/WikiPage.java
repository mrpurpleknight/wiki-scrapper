package com.lastminute.recruitment.domain.model;

import java.util.List;

public record WikiPage(String title, String content, String selfLink, List<String> links) {
    public WikiPage {
        if (selfLink == null || selfLink.isBlank()) {
            throw new IllegalArgumentException("selfLink must not be null or blank");
        }
    }
}
