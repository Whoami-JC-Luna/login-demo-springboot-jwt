package com.jcluna.auth_api.mapper;


import com.jcluna.auth_api.dto.QuoteRequest;
import com.jcluna.auth_api.dto.QuoteResponse;
import com.jcluna.auth_api.model.Quote;
import org.mapstruct.Mapper;

// componentModel = "spring" registers this mapper as a Spring bean, allowing constructor injection in services.
// MappingConstants.ComponentModel.SPRING is a type-safe alternative to the string "spring" (future improvement).
@Mapper(componentModel = "spring")
public interface QuoteMapper {

    QuoteResponse toResponse(Quote quote);

    Quote toEntity(QuoteRequest request);

}
