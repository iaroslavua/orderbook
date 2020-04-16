package com.bit.orderbook.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class HtmlController {

    @GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Object> getIndex() {
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create("/index.html")).build();
    }
}
