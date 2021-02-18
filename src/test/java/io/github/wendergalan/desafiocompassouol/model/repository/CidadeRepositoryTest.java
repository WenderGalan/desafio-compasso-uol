package io.github.wendergalan.desafiocompassouol.model.repository;

import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CidadeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CidadeRepository repository;

    @Test
    @DisplayName("Deve buscar todas as cidades por nome.")
    public void findAllCitiesByNameTest() {
        String nome = "Campo Grande";
        Cidade cidade = criarNovaCidade(nome, "Mato Grosso do Sul");
        entityManager.persist(cidade);

        List<Cidade> cidades = repository.findAllByNomeIgnoreCaseContaining(nome);

        assertThat(cidades).hasSize(1);
        assertThat(cidades).contains(cidade);
    }

    @Test
    @DisplayName("Deve buscar todas as cidades por estado.")
    public void findAllCitiesByEstadoTest() {
        String estado = "Mato Grosso do Sul";
        Cidade cidade = criarNovaCidade("Campo Grande", estado);
        entityManager.persist(cidade);

        List<Cidade> cidades = repository.findAllByEstadoIgnoreCaseContaining(estado);

        assertThat(cidades).hasSize(1);
        assertThat(cidades).contains(cidade);
    }

    @Test
    @DisplayName("Deve obter uma cidade pelo ID.")
    public void findByIdTest() {
        Cidade city = criarNovaCidade();
        entityManager.persist(city);

        Optional<Cidade> cidadeEncontrada = repository.findById(city.getId());

        assertThat(cidadeEncontrada.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar uma cidade.")
    public void saveCityTest() {
        Cidade city = criarNovaCidade();

        Cidade cidadeSalva = repository.save(city);

        assertThat(cidadeSalva.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar uma cidade.")
    public void deleteCityTest() {
        Cidade cidade = criarNovaCidade();
        entityManager.persist(cidade);

        Cidade cidadeEncontrada = entityManager.find(Cidade.class, cidade.getId());

        repository.delete(cidadeEncontrada);

        Cidade cidadeDeletada = entityManager.find(Cidade.class, cidade.getId());
        assertThat(cidadeDeletada).isNull();
    }

    public static Cidade criarNovaCidade(String nome, String estado) {
        return Cidade.builder().nome(nome).estado(estado).build();
    }

    public static Cidade criarNovaCidade() {
        return criarNovaCidade("Campo Grande", "Mato Grosso Do Sul");
    }
}
