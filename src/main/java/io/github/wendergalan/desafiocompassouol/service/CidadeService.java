package io.github.wendergalan.desafiocompassouol.service;

import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;

import java.util.List;
import java.util.Optional;

public interface CidadeService {

    Cidade save(Cidade cidade);

    Optional<Cidade> getById(Long id);

    void delete(Cidade cidade);

    Cidade update(Cidade cidade);

    List<Cidade> findAllByNome(String nome);

    List<Cidade> findAllByEstado(String estado);
}
