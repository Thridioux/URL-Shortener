package com.erdem.urlshortener.dto;

public class CreateShortUrlRequest {
    private String longUrl;

    public CreateShortUrlRequest() {}

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}
