package io.github.wendergalan.desafiocompassouol.model.repository;

import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    List<Cidade> findAllByNomeIgnoreCaseContaining(String nome);

    List<Cidade> findAllByEstadoIgnoreCaseContaining(String nome);
}
