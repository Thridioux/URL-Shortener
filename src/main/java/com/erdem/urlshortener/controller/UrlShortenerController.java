package com.erdem.urlshortener.controller;

import com.erdem.urlshortener.dto.CreateShortUrlRequest;
import com.erdem.urlshortener.dto.ShortUrlResponse;
import com.erdem.urlshortener.service.UrlShorteningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {

    private final UrlShorteningService urlShorteningService;

    public UrlShortenerController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlResponse> createShortUrl(@RequestBody CreateShortUrlRequest request) {
        ShortUrlResponse response = urlShorteningService.createShortUrl(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortPath}")
    public ResponseEntity<Void> redirect(@PathVariable String shortPath) {
        String longUrl = urlShorteningService.getLongUrl(shortPath);
        return ResponseEntity.status(302)
                .location(java.net.URI.create(longUrl))
                .build();
    }
}
