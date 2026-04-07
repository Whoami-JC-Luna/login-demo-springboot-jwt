package com.jcluna.auth_api.service.impl;

import com.jcluna.auth_api.dto.SignatureRequest;
import com.jcluna.auth_api.dto.SignatureResponse;
import com.jcluna.auth_api.exception.SignatureAlreadyExistException;
import com.jcluna.auth_api.exception.SignatureNotFoundException;
import com.jcluna.auth_api.mapper.SignatureMapper;
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

    // Access control delegated to Spring Security. Each user can only access their own signature via authentication.


    // Constructor injection preferred over @Autowired: explicit dependencies, immutability and easier testing.
    // Lombok @RequiredArgsConstructor generates the constructor automatically for all final fields.
    private final SignatureRepository signatureRepository;

    // Mapper injection
    private final SignatureMapper signatureMapper;


    @Override
    public SignatureResponse getMySignature(User user) {
        Signature signature = signatureRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.warn("Signature not found for user: {}", user.getId());
                    return new SignatureNotFoundException("No se ha encontrado ninguna firma");
                });
        return signatureMapper.toResponse(signature);
    }

    @Override
    public SignatureResponse addMySignature(SignatureRequest request, User user) {
        signatureRepository.findByUser(user)
                .ifPresent(s -> {
                    log.warn("Signature already exist: {}", user.getId());
                    throw new SignatureAlreadyExistException("Ya tienes una firma");
                });

        Signature signature = signatureMapper.toEntity(request);
        signature.setUser(user);

        Signature saved = signatureRepository.save(signature);

        return signatureMapper.toResponse(saved);
    }


    @Override
    public SignatureResponse updateMySignature(SignatureRequest request, User user) {
        Signature signature = signatureRepository.findByUser(user)

                .orElseThrow(() -> {
                    log.warn("Signature not found on update for user: {}", user.getId());
                    return new SignatureNotFoundException("No se ha encontrado ninguna firma");
                });

        signature.setMessage(request.getMessage());
        signature.setAuthor(request.getAuthor());

        Signature saved = signatureRepository.save(signature);

        return signatureMapper.toResponse(saved);
    }

    @Override
    public void deleteMySignature(User user) {
        Signature signature = signatureRepository.findByUser(user)
                .orElseThrow(() -> {
                    // log.error: delete on missing resource may indicate unauthorized access attempt (OWASP A01:2025 - Broken Access Control)
                    log.error("Signature not found on delete for user: {}", user.getId());
                    return new SignatureNotFoundException("No se ha encontrado ninguna firma");
                });

        signatureRepository.delete(signature);
    }


    // No exceptions because an empty result is not an error.
    @Override
    public Page<SignatureResponse> getAllSignatures(Pageable pageable) {
        Page<Signature> signatures = signatureRepository.findAll(pageable);

        return signatures.map(signatureMapper::toResponse);
    }


}
