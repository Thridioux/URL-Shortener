package com.erdem.urlshortener.service;

import com.erdem.urlshortener.dto.CreateShortUrlRequest;
import com.erdem.urlshortener.dto.ShortUrlResponse;

public interface UrlShorteningService {
    ShortUrlResponse createShortUrl(CreateShortUrlRequest request);
    String getLongUrl(String shortUrl);
}
