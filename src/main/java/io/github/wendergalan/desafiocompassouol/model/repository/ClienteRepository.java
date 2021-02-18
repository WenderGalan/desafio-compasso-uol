package io.github.wendergalan.desafiocompassouol.model.repository;

import io.github.wendergalan.desafiocompassouol.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAllByNomeIgnoreCaseContaining(String nome);
}
