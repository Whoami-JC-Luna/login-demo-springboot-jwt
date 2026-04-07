package com.jcluna.auth_api.repository;


import com.jcluna.auth_api.model.Quote;
import com.jcluna.auth_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, UUID> {


     List<Quote> findByUser(User user);


    //To verify ownership before delete
    Optional<Quote> findByIdAndUser(UUID id, User user);

    // Native query required to support unaccent() for accent-insensitive search in PostgreSQL
    @Query(value = "SELECT * FROM quotes WHERE unaccent(lower(author)) LIKE '%' || unaccent(lower(:author)) || '%'",
            countQuery = "SELECT count(*) FROM quotes WHERE unaccent(lower(author)) LIKE '%' || unaccent(lower(:author)) || '%'",
            nativeQuery = true)
    Page<Quote>findByAuthorContainingIgnoreCase(@Param("author") String author, Pageable pageable);

    @Query(value = "SELECT * FROM quotes WHERE unaccent(lower(text)) LIKE '%' || unaccent(lower(:text)) || '%'",
            countQuery = "SELECT count(*) FROM quotes WHERE unaccent(lower(text)) LIKE '%' || unaccent(lower(:text)) || '%'",
            nativeQuery = true)
    Page<Quote> findByTextContainingIgnoreCase(@Param("text") String text, Pageable pageable);
}
