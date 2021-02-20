package io.github.wendergalan.desafiocompassouol.service.impl;

import io.github.wendergalan.desafiocompassouol.model.entity.Cliente;
import io.github.wendergalan.desafiocompassouol.model.repository.ClienteRepository;
import io.github.wendergalan.desafiocompassouol.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository repository;

    @Override
    public List<Cliente> findAllByNome(String nome) {
        if (nome == null || nome.isEmpty())
            throw new IllegalArgumentException("O nome do cliente não pode ser nulo ou vazio.");
        return repository.findAllByNomeIgnoreCaseContaining(nome);
    }

    @Override
    public Cliente save(Cliente cliente) {
        return repository.save(cliente);
    }

    @Override
    public Optional<Cliente> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Cliente cliente) {
        if (cliente == null || cliente.getId() == null)
            throw new IllegalArgumentException("ID do cliente não pode ser nulo");
        this.repository.delete(cliente);
    }

    @Override
    public Cliente update(Cliente cliente) {
        if (cliente == null || cliente.getId() == null)
            throw new IllegalArgumentException("ID do cliente não pode ser nulo");
        return this.repository.save(cliente);
    }
}
