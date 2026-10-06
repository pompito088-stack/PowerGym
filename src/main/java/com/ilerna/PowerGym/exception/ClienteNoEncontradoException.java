package com.ilerna.PowerGym.exception;

public class ClienteNoEncontradoException extends RuntimeException {

    public ClienteNoEncontradoException(Long id) {
        super("No se encontro el cliente con id " + id);
    }
}
