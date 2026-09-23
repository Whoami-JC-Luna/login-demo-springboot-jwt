package com.jcluna.auth_api.controller;


import com.jcluna.auth_api.dto.SignatureRequest;
import com.jcluna.auth_api.dto.SignatureResponse;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.service.SignatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/signature")
@RequiredArgsConstructor
public class SignatureController {

    private final SignatureService signatureService;


    @GetMapping("/me")
    public ResponseEntity<SignatureResponse> getSignature(
        @AuthenticationPrincipal User user){
        return ResponseEntity.ok(signatureService.getMySignature(user));
    }

    @PostMapping()
    public ResponseEntity<SignatureResponse> addSignature(
            @Valid @RequestBody SignatureRequest request,
            @AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(signatureService.addMySignature(request, user));
    }

    @PutMapping()
    public ResponseEntity<SignatureResponse> updateSignature(
            @Valid @RequestBody SignatureRequest request,
            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(signatureService.updateMySignature(request, user));
    }


    @DeleteMapping()
    public ResponseEntity<Void> deleteSignature(
            @AuthenticationPrincipal User user){
        signatureService.deleteMySignature(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<Page<SignatureResponse>> getAllSignatures(Pageable pageable){
        return ResponseEntity.ok(signatureService.getAllSignatures(pageable));
    }




}
