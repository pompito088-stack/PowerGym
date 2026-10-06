package com.ilerna.PowerGym.service;

import com.ilerna.PowerGym.model.Cliente;

import java.util.List;

public interface ClienteService {

    List<Cliente> listarTodos();

    Cliente buscarPorId(Long id);

    Cliente guardar(Cliente cliente);

    void eliminar(Long id);
}
