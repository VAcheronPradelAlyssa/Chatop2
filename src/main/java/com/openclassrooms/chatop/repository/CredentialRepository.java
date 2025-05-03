package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Integer> {
    Optional<Credential> findByConfigKey(String configKey);
}
