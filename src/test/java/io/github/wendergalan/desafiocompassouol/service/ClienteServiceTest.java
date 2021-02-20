package io.github.wendergalan.desafiocompassouol.service;

import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;
import io.github.wendergalan.desafiocompassouol.model.entity.Cliente;
import io.github.wendergalan.desafiocompassouol.model.repository.CidadeRepository;
import io.github.wendergalan.desafiocompassouol.model.repository.ClienteRepository;
import io.github.wendergalan.desafiocompassouol.service.impl.ClienteServiceImpl;
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

import static io.github.wendergalan.desafiocompassouol.model.repository.ClienteRepositoryTest.criarNovoCliente;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ClienteServiceTest {

    ClienteService service;

    @MockBean
    ClienteRepository repository;

    @MockBean
    CidadeRepository cidadeRepository;

    @BeforeEach
    public void setUp() {
        this.service = new ClienteServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar buscar um cliente por nome inválido.")
    public void findInvalidClientByNameTest() {
        Cliente cliente = new Cliente();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.findAllByNome(cliente.getNome()));

        Mockito.verify(repository, Mockito.never()).findAllByNomeIgnoreCaseContaining(cliente.getNome());
    }

    @Test
    @DisplayName("Deve buscar o cliente por nome.")
    public void findClientByNameTest() {
        Cliente cliente = criarNovoCliente(new Cidade());

        when(repository.findAllByNomeIgnoreCaseContaining(cliente.getNome()))
                .thenReturn(Collections.singletonList(Cliente.builder()
                        .id(10L)
                        .nome("Wender Galan").build()));

        List<Cliente> clientes = service.findAllByNome(cliente.getNome());

        assertThat(clientes).hasSize(1);
        assertThat(clientes).contains(cliente.withId(10L));
    }

    @Test
    @DisplayName("Deve salvar um cliente.")
    public void saveClientTest() {
        Cliente cliente = criarNovoCliente(new Cidade());

        when(repository.save(cliente))
                .thenReturn(
                        Cliente.builder()
                                .id(10L)
                                .nome("Wender Galan")
                                .idade(23)
                                .build());

        Cliente clienteSalvo = service.save(cliente);

        assertThat(clienteSalvo.getId()).isNotNull();
        assertThat(clienteSalvo.getNome()).isEqualTo("Wender Galan");
        assertThat(clienteSalvo.getIdade()).isEqualTo(23);
    }

    @Test
    @DisplayName("Deve obter um cliente por ID.")
    public void getByIdTest() {
        Long id = 11L;

        Cliente cliente = criarNovoCliente(new Cidade());
        cliente.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(cliente));

        Optional<Cliente> clienteEncontrado = service.getById(id);

        assertThat(clienteEncontrado.isPresent()).isTrue();
        assertThat(clienteEncontrado.get().getId()).isEqualTo(cliente.getId());
        assertThat(clienteEncontrado.get().getNome()).isEqualTo(cliente.getNome());
        assertThat(clienteEncontrado.get().getIdade()).isEqualTo(cliente.getIdade());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um cliente por ID quando não existir na base.")
    public void cityNotFoundByIdTest() {
        Long id = 11L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Cliente> book = service.getById(id);

        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve deletar um cliente.")
    public void deleteCityTest() {
        Cliente cliente = Cliente.builder().id(11L).build();

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(cliente));

        Mockito.verify(repository, Mockito.times(1)).delete(cliente);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar deletar um cliente inexistente.")
    public void deleteInvalidClientTest() {
        Cliente cliente = new Cliente();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(cliente));

        Mockito.verify(repository, Mockito.never()).delete(cliente);
    }

    @Test
    @DisplayName("Deve atualizar um cliente")
    public void updateClientTest() {
        Long id = 11L;

        Cliente clienteParaAtualizar = criarNovoCliente(new Cidade()).withId(id);

        Cliente clienteAtualizado = criarNovoCliente(new Cidade());
        clienteAtualizado.setId(id);

        when(repository.save(clienteParaAtualizar)).thenReturn(clienteAtualizado);

        Cliente cliente = service.update(clienteParaAtualizar);

        assertThat(cliente.getId()).isEqualTo(clienteParaAtualizar.getId());
        assertThat(cliente.getNome()).isEqualTo(clienteParaAtualizar.getNome());
        assertThat(cliente.getNascimento()).isEqualTo(clienteParaAtualizar.getNascimento());
        assertThat(cliente.getIdade()).isEqualTo(clienteParaAtualizar.getIdade());
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar atualizar um cliente inexistente.")
    public void updateInvalidCityTest() {
        Cliente cliente = new Cliente();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(cliente));

        Mockito.verify(repository, Mockito.never()).save(cliente);
    }
}
