package com.jcluna.auth_api.controller;

import com.jcluna.auth_api.dto.QuoteRequest;
import com.jcluna.auth_api.dto.QuoteResponse;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.service.QuoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quotes")
public class QuoteController {

    // Constructor injection preferred over @Autowired: explicit dependencies, immutability and easier testing.
    // Lombok @RequiredArgsConstructor generates the constructor automatically for all final fields.
    private final QuoteService quoteService;




    @GetMapping("/random")
    public ResponseEntity<QuoteResponse> getRandomQuote() {
        return ResponseEntity.ok(quoteService.getRandomQuote());
    }

    @GetMapping("/me")
    public ResponseEntity <List<QuoteResponse>> getMyQuotes(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(quoteService.getMyQuote(user));
    }


    @GetMapping()
    public ResponseEntity<Page<QuoteResponse>> getAllQuotes(Pageable pageable) {
        return ResponseEntity.ok(quoteService.getAllQuotes(pageable));
    }


    // Single endpoint handles both search types. Returns 400 if no valid param is provided.
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