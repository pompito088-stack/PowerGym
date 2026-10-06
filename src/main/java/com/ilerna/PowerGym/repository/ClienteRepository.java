package com.ilerna.PowerGym.repository;

import com.ilerna.PowerGym.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    List<Cliente> findAll();

    Optional<Cliente> findById(Long id);

    boolean existsById(Long id);

    boolean existsByDni(String dni);

    boolean existsByDniAndIdNot(String dni, Long id);

    Cliente save(Cliente cliente);

    void deleteById(Long id);
}
