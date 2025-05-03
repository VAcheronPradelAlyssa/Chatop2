package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.entity.Credential;
import com.openclassrooms.chatop.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;

    public String getValueByKey(String key) {
        return credentialRepository.findByConfigKey(key)
                .orElseThrow(() -> new RuntimeException("Clé de configuration non trouvée : " + key))
                .getConfigValue();
    }
}
