package com.ilerna.PowerGym.service.impl;

import com.ilerna.PowerGym.exception.ClienteNoEncontradoException;
import com.ilerna.PowerGym.exception.DniDuplicadoException;
import com.ilerna.PowerGym.model.Cliente;
import com.ilerna.PowerGym.repository.ClienteRepository;
import com.ilerna.PowerGym.service.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(id));
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        cliente.setDni(cliente.getDni().toUpperCase());
        boolean duplicado = cliente.getId() == null
                ? clienteRepository.existsByDni(cliente.getDni())
                : clienteRepository.existsByDniAndIdNot(cliente.getDni(), cliente.getId());
        if (duplicado) {
            throw new DniDuplicadoException(cliente.getDni());
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNoEncontradoException(id);
        }
        clienteRepository.deleteById(id);
    }
}
