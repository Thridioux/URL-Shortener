package com.erdem.urlshortener.dto;

public class ShortUrlResponse {
    private String shortUrl;
    private String uuid;

    public ShortUrlResponse() {}

    public ShortUrlResponse(String shortUrl, String uuid) {
        this.shortUrl = shortUrl;
        this.uuid = uuid;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
