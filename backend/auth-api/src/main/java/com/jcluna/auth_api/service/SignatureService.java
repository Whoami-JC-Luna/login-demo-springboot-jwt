package com.jcluna.auth_api.service;

import com.jcluna.auth_api.dto.SignatureRequest;
import com.jcluna.auth_api.dto.SignatureResponse;
import com.jcluna.auth_api.model.Quote;
import com.jcluna.auth_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SignatureService {

    // Get the user's signature after login to show it in the dashboard if it exists
    SignatureResponse getMySignature(User user);

    SignatureResponse addMySignature(SignatureRequest request, User user);

    SignatureResponse updateMySignature(SignatureRequest request, User user);

    void deleteMySignature(User user);

    // Get all signatures to display them globally to users.
    Page<SignatureResponse> getAllSignature(Pageable pageable);

}
