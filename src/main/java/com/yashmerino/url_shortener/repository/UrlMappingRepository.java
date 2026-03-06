package com.yashmerino.url_shortener.repository;

import com.yashmerino.url_shortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * URL Mapping JPA entity repository.
 */
@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
}
