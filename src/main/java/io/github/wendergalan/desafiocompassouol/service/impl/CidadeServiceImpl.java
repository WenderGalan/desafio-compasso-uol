package io.github.wendergalan.desafiocompassouol.service.impl;

import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;
import io.github.wendergalan.desafiocompassouol.model.repository.CidadeRepository;
import io.github.wendergalan.desafiocompassouol.service.CidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CidadeServiceImpl implements CidadeService {
    private final CidadeRepository repository;

    @Override
    public Cidade save(Cidade cidade) {
        return repository.save(cidade);
    }

    @Override
    public Optional<Cidade> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Cidade cidade) {
        if (cidade == null || cidade.getId() == null)
            throw new IllegalArgumentException("ID da cidade não pode ser nulo");
        this.repository.delete(cidade);
    }

    @Override
    public Cidade update(Cidade cidade) {
        if (cidade == null || cidade.getId() == null)
            throw new IllegalArgumentException("ID da cidade não pode ser nulo");
        return this.repository.save(cidade);
    }

    @Override
    public List<Cidade> findAllByNome(String nome) {
        if (nome == null || nome.isEmpty())
            throw new IllegalArgumentException("O nome da cidade não pode ser nulo ou vazio.");
        return repository.findAllByNomeIgnoreCaseContaining(nome);
    }

    @Override
    public List<Cidade> findAllByEstado(String estado) {
        if (estado == null || estado.isEmpty())
            throw new IllegalArgumentException("O estado não pode ser nulo ou vazio.");
        return repository.findAllByEstadoIgnoreCaseContaining(estado);
    }
}
