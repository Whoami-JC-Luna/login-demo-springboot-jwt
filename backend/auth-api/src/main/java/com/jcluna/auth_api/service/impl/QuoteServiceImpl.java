package com.jcluna.auth_api.service.impl;


import com.jcluna.auth_api.dto.QuoteRequest;
import com.jcluna.auth_api.dto.QuoteResponse;
import com.jcluna.auth_api.exception.QuoteNotFoundException;
import com.jcluna.auth_api.mapper.QuoteMapper;
import com.jcluna.auth_api.mapper.SignatureMapper;
import com.jcluna.auth_api.model.Quote;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.repository.QuoteRepository;
import com.jcluna.auth_api.service.QuoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    // Constructor injection preferred over @Autowired: explicit dependencies, immutability and easier testing.
    // Lombok @RequiredArgsConstructor generates the constructor automatically for all final fields.
    private final QuoteRepository quoteRepository;

    // Mapper injection
    private final QuoteMapper quoteMapper;
    private final SignatureMapper signatureMapper;


    @Override
    public QuoteResponse getRandomQuote() {
        List<Quote> quotes = quoteRepository.findAll();
        if (quotes.isEmpty()) {
            // Random needs at least one quote to work, unlike listings that can return empty.
            // Empty DB is unexpected since quotes are preloaded by Flyway, so I warn.
            log.warn("No quotes available to return a random quote");
            throw new QuoteNotFoundException("No se encontró el recurso");
        }
        Quote random = quotes.get(new Random().nextInt(quotes.size()));
        return quoteMapper.toResponse(random);
    }



    // This is not an error, just return 200 OK with an empty result.
    @Override
    public Page<QuoteResponse> getAllQuotes(Pageable pageable) {
        Page<Quote> quotes = quoteRepository.findAll(pageable);
        return quotes.map(quoteMapper::toResponse);
    }

    @Override
    public Page<QuoteResponse> searchByAuthor(String author, Pageable pageable) {
        Page<Quote> quotes = quoteRepository.findByAuthorContainingIgnoreCase(author, pageable);
        return quotes.map(quoteMapper::toResponse);
    }

    @Override
    public Page<QuoteResponse> searchByText(String text, Pageable pageable) {
        Page<Quote> quotes = quoteRepository.findByTextContainingIgnoreCase(text, pageable);
        return quotes.map(quoteMapper::toResponse);
    }



    @Override
    public QuoteResponse addQuote(QuoteRequest request, User user) {
        Quote quote = quoteMapper.toEntity(request);
        quote.setUser(user);

        quoteRepository.save(quote);
        log.info("New quote created: {}", quote.getId());
        return quoteMapper.toResponse(quote);
    }



    // Returns 404 for both "not found" and "not owned" cases intentionally.(OWASP A01:2025 - Broken Access Control)
    // Avoids confirming resource existence to unauthorized users.
    @Override
    public QuoteResponse updateQuote(UUID id, QuoteRequest request, User user) {
        Quote quote = quoteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> {
                    log.error("Quote not found or not owned by user. quoteId={}, userId={}", id, user.getId());
                    return new QuoteNotFoundException("No se encontró el recurso");
                });
        quote.setText(request.getText());
        quote.setAuthor(request.getAuthor());
        quoteRepository.save(quote);
        return quoteMapper.toResponse(quote);
    }

    @Override
    public void deleteQuote(UUID id, User user) {
        Quote quote = quoteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> {
                    // log.error: delete on missing or unowned resource may indicate unauthorized access attempt.
                    log.error("Quote not found or not owned by user. quoteId={}, userId={}", id, user.getId());

                    // Generic message. Does not reveal if resource exists or belongs to another user (OWASP A01:2025 - Broken Access Control)
                    return new QuoteNotFoundException("No se encontró el recurso");
                });

        quoteRepository.delete(quote);
    }

    @Override
    public List<QuoteResponse> getMyQuote(User user) {
       List <Quote> quotes = quoteRepository.findByUser(user);
        return quotes.stream().map(quoteMapper::toResponse).toList();
    }
}
