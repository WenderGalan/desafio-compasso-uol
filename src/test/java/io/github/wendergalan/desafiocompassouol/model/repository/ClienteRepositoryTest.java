package io.github.wendergalan.desafiocompassouol.model.repository;

import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;
import io.github.wendergalan.desafiocompassouol.model.entity.Cliente;
import io.github.wendergalan.desafiocompassouol.model.enums.Sexo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.github.wendergalan.desafiocompassouol.model.repository.CidadeRepositoryTest.criarNovaCidade;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository repository;

    @Test
    @DisplayName("Deve buscar todos os clientes por nome.")
    public void findAllClientByNameTest() {
        String nome = "Wender";
        Cidade cidade = entityManager.persist(criarNovaCidade());
        Cliente cliente = criarNovoCliente(nome, Sexo.MASCULINO, cidade);
        entityManager.persist(cliente);

        List<Cliente> clientes = repository.findAllByNomeIgnoreCaseContaining(nome);

        assertThat(clientes).hasSize(1);
        assertThat(clientes).contains(cliente);
    }

    @Test
    @DisplayName("Deve obter um cliente pelo ID.")
    public void findByIdTest() {
        Cidade cidade = entityManager.persist(criarNovaCidade());
        Cliente client = criarNovoCliente(cidade);

        entityManager.persist(client);

        Optional<Cliente> clienteEncontrado = repository.findById(client.getId());

        assertThat(clienteEncontrado.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um cliente.")
    public void saveClientTest() {
        Cidade cidade = entityManager.persist(criarNovaCidade());
        Cliente client = criarNovoCliente(cidade);

        Cliente clienteSalvo = repository.save(client);

        assertThat(clienteSalvo.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um cliente.")
    public void deleteCityTest() {
        Cidade cidade = entityManager.persist(criarNovaCidade());
        Cliente cliente = criarNovoCliente(cidade);

        entityManager.persist(cliente);

        Cliente clienteEncontrado = entityManager.find(Cliente.class, cliente.getId());

        repository.delete(clienteEncontrado);

        Cliente clienteDeletado = entityManager.find(Cliente.class, cliente.getId());
        assertThat(clienteDeletado).isNull();
    }

    public static Cliente criarNovoCliente(String nome, Sexo sexo, Cidade cidade) {
        return Cliente.builder()
                .nome(nome)
                .sexo(sexo)
                .nascimento(LocalDate.now())
                .idade(0)
                .cidade(cidade)
                .build();
    }

    public static Cliente criarNovoCliente(Cidade cidade) {
        return criarNovoCliente("Wender Galan", Sexo.MASCULINO, cidade);
    }
}
