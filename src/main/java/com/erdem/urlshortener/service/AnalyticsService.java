package com.erdem.urlshortener.service;

public interface AnalyticsService {
    void recordHit(String shortPath);
}
