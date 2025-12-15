package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.rest.error.WikiInvalidLink;

import java.net.URI;
import java.util.regex.Pattern;


public final class WikiLinkValidator {

    private static final String ALLOWED_HOST = "wikiscrapper.test";
    private static final Pattern PAGE_NAME = Pattern.compile("^site\\d+$");

    public void validate(String link) {
        final URI uri;
        try {
            uri = URI.create(link);
        } catch (IllegalArgumentException e) {
            throw new WikiInvalidLink("Invalid URI format: " + link);
        }

        if (uri.getScheme() == null || uri.getHost() == null)
            throw new WikiInvalidLink("URI must include scheme and host: " + link);

        if (!uri.getScheme().equalsIgnoreCase("http") && !uri.getScheme().equalsIgnoreCase("https"))
            throw new WikiInvalidLink("Invalid URI scheme: " + uri.getScheme());

        if (!ALLOWED_HOST.equalsIgnoreCase(uri.getHost()))
            throw new WikiInvalidLink("Invalid URI host: " + uri.getHost());


        String path = uri.getPath();
        if (path.equals("/") || path.isBlank()) throw new WikiInvalidLink("URI path is missing: " + link);

        if (!path.startsWith("/") || path.indexOf('/', 1) != -1)
            throw new WikiInvalidLink("Invalid URI path: " + path);

        String page = path.substring(1);

        if (!PAGE_NAME.matcher(page).matches())
            throw new WikiInvalidLink("Invalid page name: " + page);

    }
}
