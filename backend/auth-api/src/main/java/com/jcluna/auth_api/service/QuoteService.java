package com.jcluna.auth_api.service;

import com.jcluna.auth_api.dto.QuoteRequest;
import com.jcluna.auth_api.dto.QuoteResponse;
import com.jcluna.auth_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface QuoteService {


    QuoteResponse getRandomQuote();
    Page<QuoteResponse> getAllQuotes(Pageable pageable);
    Page<QuoteResponse> searchByAuthor(String author, Pageable pageable);
    Page<QuoteResponse> searchByText(String text, Pageable pageable);
    QuoteResponse addQuote(QuoteRequest request, User user);
    QuoteResponse updateQuote(UUID id, QuoteRequest request, User user);
    void deleteQuote(UUID id, User user);


}
