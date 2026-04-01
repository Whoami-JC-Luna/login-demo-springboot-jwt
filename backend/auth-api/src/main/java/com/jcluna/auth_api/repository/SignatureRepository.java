package com.jcluna.auth_api.repository;


import com.jcluna.auth_api.model.Signature;
import com.jcluna.auth_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, UUID> {

    Optional<Signature> findByUser(User user);



}
