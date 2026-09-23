package com.jcluna.auth_api.mapper;


import com.jcluna.auth_api.dto.SignatureRequest;
import com.jcluna.auth_api.dto.SignatureResponse;
import com.jcluna.auth_api.model.Signature;
import org.mapstruct.Mapper;

// componentModel = "spring" registers this mapper as a Spring bean, allowing constructor injection in services.
// MappingConstants.ComponentModel.SPRING is a type-safe alternative to the string "spring" (future improvement).
@Mapper(componentModel = "spring")
public interface SignatureMapper {

    SignatureResponse toResponse(Signature signature);

    Signature toEntity(SignatureRequest request);
}
