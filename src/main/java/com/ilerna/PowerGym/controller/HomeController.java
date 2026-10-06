package com.ilerna.PowerGym.controller;

import com.ilerna.PowerGym.service.TransaccionLogger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tools.jackson.databind.node.ArrayNode;

import java.io.IOException;

@Controller
public class HomeController {

    private final TransaccionLogger transaccionLogger;

    public HomeController(TransaccionLogger transaccionLogger) {
        this.transaccionLogger = transaccionLogger;
    }

    @GetMapping("/")
    public String raiz() {
        return "redirect:/clientes";
    }

    @GetMapping(value = "/api/transacciones", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ArrayNode obtenerTransacciones() throws IOException {
        return transaccionLogger.obtenerTransacciones();
    }
}
