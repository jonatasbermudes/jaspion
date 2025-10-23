package com.alertsfromtv.controller;

import com.alertsfromtv.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private WebhookService webhookService;

    @PostMapping("/{conexaoId}")
    public ResponseEntity<?> receiveWebhook(@PathVariable UUID conexaoId, @RequestBody String body) {
        webhookService.processarWebhook(conexaoId, body);
        return ResponseEntity.ok().build();
    }
}
