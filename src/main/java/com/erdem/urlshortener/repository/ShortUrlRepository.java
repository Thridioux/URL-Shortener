package com.erdem.urlshortener.repository;

import com.erdem.urlshortener.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, String> {
    Optional<ShortUrl> findByShortUrl(String shortUrl);
}
