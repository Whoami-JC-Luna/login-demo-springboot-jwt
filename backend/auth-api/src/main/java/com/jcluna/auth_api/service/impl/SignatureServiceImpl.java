package com.jcluna.auth_api.service.impl;

import com.jcluna.auth_api.dto.SignatureRequest;
import com.jcluna.auth_api.dto.SignatureResponse;
import com.jcluna.auth_api.exception.SignatureAlreadyExistException;
import com.jcluna.auth_api.exception.SignatureNotFoundException;
import com.jcluna.auth_api.model.Signature;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.repository.SignatureRepository;
import com.jcluna.auth_api.service.SignatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignatureServiceImpl implements SignatureService {

    private final SignatureRepository signatureRepository;


    @Override
    public SignatureResponse getMySignature(User user) {
        Signature signature = signatureRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.error("Signature not found for user: {}", user.getId());
                    return new SignatureNotFoundException("No se ha encontrado ninguna firma");
                });


        return new SignatureResponse(
                signature.getSignature(),
                signature.getAuthor()
        );
    }

    @Override
    public SignatureResponse createMySignature(SignatureRequest request, User user) {
        signatureRepository.findByUser(user)
                .ifPresent(s -> {
                    log.error("Signature already exist");
                    throw new SignatureAlreadyExistException("Ya tienes una firma");
                });

        Signature signature = new Signature();
        signature.setSignature(request.getSignature());
        signature.setAuthor(request.getAuthor());
        signature.setUser(user);

        Signature saved = signatureRepository.save(signature);

        return new SignatureResponse(saved.getSignature(), saved.getAuthor());
    }


    @Override
    public SignatureResponse updateMySignature(SignatureRequest request, User user) {
        Signature signature = signatureRepository.findByUser(user)

                .orElseThrow(() -> {
                    log.error("Signature not found on update for user: {}", user.getId());
                    return new SignatureNotFoundException("No se ha encontrado ninguna firma");
                });

        signature.setSignature(request.getSignature());
        signature.setAuthor(request.getAuthor());

        Signature saved = signatureRepository.save(signature);

        return new SignatureResponse(saved.getSignature(), saved.getAuthor());

    }

    @Override
    public void deleteMySignature(User user) {
        Signature signature = signatureRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.error("Signature not found on delete for user: {}", user.getId());
                    return new SignatureNotFoundException("No se ha encontrado ninguna firma");
                });

        signatureRepository.delete(signature);
    }


    // No exceptions because an empty result is not an error.
    @Override
    public Page<SignatureResponse> getAllSignature(Pageable pageable) {
        Page<Signature> signatures = signatureRepository.findAll(pageable);
        return signatures.map(signature -> new SignatureResponse(signature.getSignature(), signature.getAuthor()));
    }


}
