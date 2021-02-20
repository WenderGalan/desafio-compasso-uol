package io.github.wendergalan.desafiocompassouol.service;

import io.github.wendergalan.desafiocompassouol.model.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    List<Cliente> findAllByNome(String nome);

    Cliente save(Cliente cliente);

    Optional<Cliente> getById(Long id);

    void delete(Cliente cliente);

    Cliente update(Cliente cliente);
}
