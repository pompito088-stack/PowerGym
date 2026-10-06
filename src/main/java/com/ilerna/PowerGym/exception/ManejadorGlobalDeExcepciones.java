package com.ilerna.PowerGym.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ManejadorGlobalDeExcepciones {

    @ExceptionHandler(ClienteNoEncontradoException.class)
    public String manejarClienteNoEncontrado(ClienteNoEncontradoException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/clientes";
    }
}
