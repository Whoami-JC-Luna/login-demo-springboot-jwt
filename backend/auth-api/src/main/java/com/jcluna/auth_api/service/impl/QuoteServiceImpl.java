package com.jcluna.auth_api.service.impl;


import com.jcluna.auth_api.dto.QuoteRequest;
import com.jcluna.auth_api.dto.QuoteResponse;
import com.jcluna.auth_api.exception.QuoteNotFoundException;
import com.jcluna.auth_api.model.Quote;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.repository.QuoteRepository;
import com.jcluna.auth_api.service.QuoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class QuoteServiceImpl implements QuoteService {


    private final QuoteRepository quoteRepository;


    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }


    @Override
    public QuoteResponse getRandomQuote() {
        List<Quote> quotes = quoteRepository.findAll();
        if (quotes.isEmpty()) {
            log.error("No quotes available to return a random quote");
            throw new QuoteNotFoundException("No se encontró el recurso");
        }
        Quote random = quotes.get(new Random().nextInt(quotes.size()));
        return new QuoteResponse(random.getText(), random.getAuthor());
    }


    // If no results are found, don't throw an exception.
    // This is not an error, just return 200 OK with an empty result.
    @Override
    public Page<QuoteResponse> getAllQuotes(Pageable pageable) {
        Page<Quote> quotes = quoteRepository.findAll(pageable);
        return quotes.map(quote -> new QuoteResponse(quote.getText(), quote.getAuthor()));
    }

    @Override
    public Page<QuoteResponse> searchByAuthor(String author, Pageable pageable) {
        Page<Quote> quotes = quoteRepository.findByAuthorContainingIgnoreCase(author, pageable);
        return quotes.map(quote -> new QuoteResponse(quote.getText(), quote.getAuthor()));
    }

    @Override
    public Page<QuoteResponse> searchByText(String text, Pageable pageable) {
        Page<Quote> quotes = quoteRepository.findByTextContainingIgnoreCase(text, pageable);
        return quotes.map(quote -> new QuoteResponse(quote.getText(), quote.getAuthor()));
    }





    @Override
    public QuoteResponse addQuote(QuoteRequest request, User user) {
        Quote quote = new Quote();
        quote.setText(request.getText());
        quote.setAuthor(request.getAuthor());
        quote.setUser(user);
        quoteRepository.save(quote);
        return new QuoteResponse(quote.getText(), quote.getAuthor());
    }



    // Returns 404 for both "not found" and "not owned" cases intentionally.
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
        return new QuoteResponse(quote.getText(), quote.getAuthor());
    }

    @Override
    public void deleteQuote(UUID id, User user) {
        Quote quote = quoteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> {
                    log.error("Quote not found or not owned by user. quoteId={}, userId={}", id, user.getId());
                    return new QuoteNotFoundException("No se encontró el recurso");
                });

        quoteRepository.delete(quote);
    }
}
