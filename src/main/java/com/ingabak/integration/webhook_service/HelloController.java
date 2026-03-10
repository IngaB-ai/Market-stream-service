package com.ingabak.integration.webhook_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String home() {
        return "Websocket Service Running";
    }
}
