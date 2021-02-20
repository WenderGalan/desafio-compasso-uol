package io.github.wendergalan.desafiocompassouol.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.wendergalan.desafiocompassouol.api.dto.ClienteDTO;
import io.github.wendergalan.desafiocompassouol.api.resource.ClienteController;
import io.github.wendergalan.desafiocompassouol.config.converter.deserializer.LocalDateDeserializer;
import io.github.wendergalan.desafiocompassouol.config.converter.serializer.LocalDateSerializer;
import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;
import io.github.wendergalan.desafiocompassouol.model.entity.Cliente;
import io.github.wendergalan.desafiocompassouol.model.enums.Sexo;
import io.github.wendergalan.desafiocompassouol.service.ClienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static io.github.wendergalan.desafiocompassouol.model.repository.ClienteRepositoryTest.criarNovoCliente;
import static io.github.wendergalan.desafiocompassouol.utility.Constants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ClienteController.class)
@AutoConfigureMockMvc
@WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLES)
public class ClienteControllerTest {

    static String CLIENTE_API = "/clientes";

    @Autowired
    MockMvc mvc;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    ClienteService service;

    @Test
    @DisplayName("Deve criar um cliente com sucesso.")
    public void createClientTest() throws Exception {
        ClienteDTO dto = criarNovoClienteDto(new Cidade().withId(1L)).withSexo(Sexo.MASCULINO);
        Cliente clienteSalvo = Cliente.builder().id(11L).nome("Wender Galan").idade(23).sexo(Sexo.MASCULINO).nascimento(LocalDate.now()).build();

        BDDMockito.given(service.save(Mockito.any(Cliente.class))).willReturn(clienteSalvo);

        String json = objectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(11L))
                .andExpect(jsonPath("idade").value(dto.getIdade()))
                .andExpect(jsonPath("sexo").value(dto.getSexo().toString()));
    }

    @Test
    @DisplayName("Deve lançar um erro de validação quando não houver dados suficientes para a criação do cliente.")
    public void createInvalidClientTest() throws Exception {
        String json = objectMapper().writeValueAsString(new ClienteDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(4)));
    }

    @Test
    @DisplayName("Deve obter informações de um cliente.")
    public void getClientDetailsTest() throws Exception {
        Long id = 11L;

        Cliente cliente = Cliente.builder()
                .id(id)
                .nome(criarNovoClienteDto().getNome())
                .nascimento(criarNovoClienteDto().getNascimento())
                .sexo(criarNovoClienteDto().getSexo())
                .idade(criarNovoClienteDto().getIdade())
                .build();

        BDDMockito.given(service.getById(id)).willReturn(Optional.of(cliente));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CLIENTE_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(11L))
                .andExpect(jsonPath("nome").value(criarNovoClienteDto().getNome()))
                .andExpect(jsonPath("sexo").value(criarNovoClienteDto().getSexo().toString()))
                .andExpect(jsonPath("idade").value(criarNovoClienteDto().getIdade()));
    }

    @Test
    @DisplayName("Deve retornar resource not found quando o cliente procurado não existir.")
    public void clientNotFoundTest() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CLIENTE_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um cliente.")
    public void deleteClientTest() throws Exception {

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.of(Cliente.builder().id(11L).build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CLIENTE_API.concat("/" + 11))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar resource not found quando não encontar um cliente para deletar.")
    public void deleteInexistentCityTest() throws Exception {

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CLIENTE_API.concat("/" + 11))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar um cliente.")
    public void updateClientTest() throws Exception {
        Long id = 11L;
        String json = objectMapper().writeValueAsString(criarNovoClienteDto());

        Cliente clienteAtualizando = Cliente.builder().id(id).nome("Algum nome").idade(24).build();
        BDDMockito.given(service.getById(anyLong()))
                .willReturn(Optional.of(clienteAtualizando));

        Cliente clienteAtualizar = Cliente.builder().id(id).nome("Wender Galan").idade(23).build();
        BDDMockito.given(service.update(clienteAtualizando))
                .willReturn(clienteAtualizar);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CLIENTE_API.concat("/" + id))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("nome").value(criarNovoClienteDto().getNome()))
                .andExpect(jsonPath("idade").value(criarNovoClienteDto().getIdade()));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar um cliente inexistente.")
    public void updateInexistentClientTest() throws Exception {
        String json = objectMapper().writeValueAsString(criarNovoClienteDto());
        BDDMockito.given(service.getById(anyLong()))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CLIENTE_API.concat("/" + 11))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar todos os clientes filtrado pelo nome.")
    public void findAllClientsByNameTest() throws Exception {
        Cliente cliente = criarNovoCliente(new Cidade());

        BDDMockito.given(service.findAllByNome(anyString()))
                .willReturn(Collections.singletonList(cliente));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CLIENTE_API.concat("/search"))
                .param("nome", "Wender")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk());
    }

    private ClienteDTO criarNovoClienteDto() {
        return criarNovoClienteDto(new Cidade());
    }

    private ClienteDTO criarNovoClienteDto(Cidade cidade) {
        return ClienteDTO.builder()
                .nome("Wender Galan")
                .idade(23)
                .cidade(cidade)
                .sexo(Sexo.MASCULINO)
                .nascimento(LocalDate.now())
                .build();
    }

    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        mapper.registerModule(javaTimeModule);
        return mapper;
    }
}
