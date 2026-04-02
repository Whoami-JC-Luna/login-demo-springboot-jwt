package com.jcluna.auth_api.controller;

import com.jcluna.auth_api.dto.QuoteRequest;
import com.jcluna.auth_api.dto.QuoteResponse;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/random")
    public ResponseEntity<QuoteResponse> Rote() {
        return ResponseEntity.ok(quoteService.getRandomQuote());
    }

    @GetMapping("")
    public ResponseEntity<Page<QuoteResponse>> getAllQuotes(Pageable pageable) {
        return ResponseEntity.ok(quoteService.getAllQuotes(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<QuoteResponse>> searchByAuthor(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String text,
            Pageable pageable) {
        if (author != null) {
            return ResponseEntity.ok(quoteService.searchByAuthor(author, pageable));
        }
        if (text != null) {
            return ResponseEntity.ok(quoteService.searchByText(text, pageable));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<QuoteResponse> addQuote(
            @Valid @RequestBody QuoteRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quoteService.addQuote(request, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuoteResponse> updateQuote(
            @PathVariable UUID id,
            @Valid @RequestBody QuoteRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(quoteService.updateQuote(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuote(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        quoteService.deleteQuote(id, user);
        return ResponseEntity.noContent().build();
    }
}