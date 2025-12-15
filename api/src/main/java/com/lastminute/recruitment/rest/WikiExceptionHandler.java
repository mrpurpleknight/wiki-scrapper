package com.lastminute.recruitment.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lastminute.recruitment.domain.error.WikiInvalidPage;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import com.lastminute.recruitment.rest.error.WikiInvalidLink;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WikiExceptionHandler {

    @ExceptionHandler(WikiPageNotFound.class)
    public ResponseEntity<String> handleWikiPageNotFound(WikiPageNotFound exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body(exception.getMessage());
    }

    @ExceptionHandler(WikiInvalidPage.class)
    public ResponseEntity<String> handleWikiInvalidPage(WikiInvalidPage exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN)
                .body(exception.getMessage());
    }

    @ExceptionHandler(WikiInvalidLink.class)
    public ResponseEntity<String> handleWikiInvalidLink(WikiInvalidLink exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.TEXT_PLAIN)
                .body(exception.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessing() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Unable to decode link from request body");
    }
}