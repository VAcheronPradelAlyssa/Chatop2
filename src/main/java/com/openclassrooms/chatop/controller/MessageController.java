package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.MessageRequest;
import com.openclassrooms.chatop.dto.MessageResponse;
import com.openclassrooms.chatop.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST responsable de la gestion des messages.
 * Permet la création d'un message via une requête POST.
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * Crée un nouveau message.
     *
     * @param request Les données du message à créer (expéditeur, destinataire, message)
     * @return Une réponse contenant les informations du message créé
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse createMessage(@RequestBody @Valid MessageRequest request) {
        return messageService.createMessage(request);
    }
}
