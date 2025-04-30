package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.MessageRequest;
import com.openclassrooms.chatop.dto.MessageResponse;
import com.openclassrooms.chatop.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Messages", description = "API pour la gestion des messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * Crée un nouveau message.
     *
     * @param request Les données du message à créer (expéditeur, destinataire, message)
     * @return Une réponse contenant les informations du message créé
     */
    @Operation(summary = "Create a new message", description = "Creates a new message associated with a rental and a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Message created successfully", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse createMessage(@RequestBody @Valid MessageRequest request) {
        return messageService.createMessage(request);
    }
}
