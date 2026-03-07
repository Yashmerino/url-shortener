package com.yashmerino.url_shortener.repository;

import com.yashmerino.url_shortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * URL Mapping JPA entity repository.
 */
@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    /**
     * Verify if the short code already exists.
     *
     * @param shortCode the short code.
     *
     * @return True if the short code already exists and False otherwise.
     */
    boolean existsByShortCode(final String shortCode);

    /**
     * Returns the URL Mapping object for the corresponding short code.
     *
     * @param shortCode the short code.
     *
     * @return UrlMapping.
     */
    UrlMapping findByShortCode(final String shortCode);

    /**
     * Find all URLs created after a specific time, ordered by creation date descending.
     *
     * @param createdAfter the time threshold.
     * 
     * @return list of URL mappings created after the threshold.
     */
    List<UrlMapping> findByCreatedAtAfterAndIsArchivedFalseOrderByCreatedAtDesc(LocalDateTime createdAfter);
}
