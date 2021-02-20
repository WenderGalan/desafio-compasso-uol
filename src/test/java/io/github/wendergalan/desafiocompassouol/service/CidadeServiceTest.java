package io.github.wendergalan.desafiocompassouol.service;

import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;
import io.github.wendergalan.desafiocompassouol.model.repository.CidadeRepository;
import io.github.wendergalan.desafiocompassouol.service.impl.CidadeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.github.wendergalan.desafiocompassouol.model.repository.CidadeRepositoryTest.criarNovaCidade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CidadeServiceTest {

    CidadeService service;

    @MockBean
    CidadeRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new CidadeServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar buscar uma cidade por nome inválido.")
    public void findInvalidCityByNameTest() {
        Cidade cidade = new Cidade();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.findAllByNome(cidade.getNome()));

        Mockito.verify(repository, Mockito.never()).findAllByNomeIgnoreCaseContaining(cidade.getNome());
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar buscar uma cidade por estado inválido.")
    public void findInvalidCityByEstadoTest() {
        Cidade cidade = new Cidade();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.findAllByEstado(cidade.getEstado()));

        Mockito.verify(repository, Mockito.never()).findAllByEstadoIgnoreCaseContaining(cidade.getEstado());
    }

    @Test
    @DisplayName("Deve buscar a cidade por nome.")
    public void findCityByNameTest() {
        Cidade cidade = criarNovaCidade();

        when(repository.findAllByNomeIgnoreCaseContaining(cidade.getNome()))
                .thenReturn(Collections.singletonList(Cidade.builder()
                        .id(10L)
                        .nome("Campo Grande")
                        .estado("Mato Grosso Do Sul").build()));

        List<Cidade> cidades = service.findAllByNome(cidade.getNome());

        assertThat(cidades).hasSize(1);
        assertThat(cidades).contains(cidade.withId(10L));
    }

    @Test
    @DisplayName("Deve buscar a cidade por estado.")
    public void findCityByEstadoTest() {
        Cidade cidade = criarNovaCidade();

        when(repository.findAllByEstadoIgnoreCaseContaining(cidade.getEstado()))
                .thenReturn(Collections.singletonList(Cidade.builder()
                        .id(10L)
                        .nome("Campo Grande")
                        .estado("Mato Grosso Do Sul").build()));

        List<Cidade> cidades = service.findAllByEstado(cidade.getEstado());

        assertThat(cidades).hasSize(1);
        assertThat(cidades).contains(cidade.withId(10L));
    }

    @Test
    @DisplayName("Deve salvar uma cidade.")
    public void saveCityTest() {
        Cidade cidade = criarNovaCidade();

        when(repository.save(cidade))
                .thenReturn(
                        Cidade.builder()
                                .id(10L)
                                .nome("Campo Grande")
                                .estado("MS").build());

        Cidade cidadeSalva = service.save(cidade);

        assertThat(cidadeSalva.getId()).isNotNull();
        assertThat(cidadeSalva.getNome()).isEqualTo("Campo Grande");
        assertThat(cidadeSalva.getEstado()).isEqualTo("MS");
    }

    @Test
    @DisplayName("Deve obter uma cidade por ID.")
    public void getByIdTest() {
        Long id = 11L;

        Cidade cidade = criarNovaCidade();
        cidade.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(cidade));

        Optional<Cidade> cidadeEncontrada = service.getById(id);

        assertThat(cidadeEncontrada.isPresent()).isTrue();
        assertThat(cidadeEncontrada.get().getId()).isEqualTo(cidade.getId());
        assertThat(cidadeEncontrada.get().getNome()).isEqualTo(cidade.getNome());
        assertThat(cidadeEncontrada.get().getEstado()).isEqualTo(cidade.getEstado());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter uma cidade por ID quando não existir na base.")
    public void cityNotFoundByIdTest() {
        Long id = 11L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Cidade> book = service.getById(id);

        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve deletar uma cidade.")
    public void deleteCityTest() {
        Cidade cidade = Cidade.builder().id(11L).build();

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(cidade));

        Mockito.verify(repository, Mockito.times(1)).delete(cidade);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar deletar uma cidade inexistente.")
    public void deleteInvalidCityTest() {
        Cidade cidade = new Cidade();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(cidade));

        Mockito.verify(repository, Mockito.never()).delete(cidade);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar atualizar uma cidade inexistente.")
    public void updateInvalidCityTest() {
        Cidade cidade = new Cidade();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(cidade));

        Mockito.verify(repository, Mockito.never()).save(cidade);
    }

    @Test
    @DisplayName("Deve atualizar uma cidade")
    public void updateCityTest() {
        Long id = 11L;

        Cidade cidadeParaAtualizar = criarNovaCidade().withId(id);

        Cidade cidadeAtualizado = criarNovaCidade();
        cidadeAtualizado.setId(id);

        when(repository.save(cidadeParaAtualizar)).thenReturn(cidadeAtualizado);

        Cidade cidade = service.update(cidadeParaAtualizar);

        assertThat(cidade.getId()).isEqualTo(cidadeParaAtualizar.getId());
        assertThat(cidade.getNome()).isEqualTo(cidadeParaAtualizar.getNome());
        assertThat(cidade.getEstado()).isEqualTo(cidadeParaAtualizar.getEstado());
    }
}
