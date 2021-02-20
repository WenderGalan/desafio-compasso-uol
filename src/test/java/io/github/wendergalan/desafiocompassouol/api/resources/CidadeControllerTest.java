package io.github.wendergalan.desafiocompassouol.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.wendergalan.desafiocompassouol.api.dto.CidadeDTO;
import io.github.wendergalan.desafiocompassouol.api.resource.CidadeController;
import io.github.wendergalan.desafiocompassouol.config.converter.deserializer.LocalDateDeserializer;
import io.github.wendergalan.desafiocompassouol.config.converter.serializer.LocalDateSerializer;
import io.github.wendergalan.desafiocompassouol.model.entity.Cidade;
import io.github.wendergalan.desafiocompassouol.service.CidadeService;
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

import static io.github.wendergalan.desafiocompassouol.model.repository.CidadeRepositoryTest.criarNovaCidade;
import static io.github.wendergalan.desafiocompassouol.utility.Constants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CidadeController.class)
@AutoConfigureMockMvc
@WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLES)
public class CidadeControllerTest {

    static String CIDADE_API = "/cidades";

    @Autowired
    MockMvc mvc;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    CidadeService service;

    @Test
    @DisplayName("Deve criar uma cidade com sucesso.")
    public void createCityTest() throws Exception {
        CidadeDTO dto = criarNovaCidadeDto();
        Cidade cidadeSalva = Cidade.builder().id(11L).nome("Campo Grande").estado("MS").build();

        BDDMockito.given(service.save(Mockito.any(Cidade.class))).willReturn(cidadeSalva);

        String json = objectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CIDADE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(11L))
                .andExpect(jsonPath("nome").value(dto.getNome()))
                .andExpect(jsonPath("estado").value(dto.getEstado()));
    }

    @Test
    @DisplayName("Deve lançar um erro de validação quando não houver dados suficientes para a criação de uma cidade.")
    public void createInvalidCityTest() throws Exception {
        String json = objectMapper().writeValueAsString(new CidadeDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CIDADE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(2)));
    }

    @Test
    @DisplayName("Deve obter informações de uma cidade.")
    public void getCityDetailsTest() throws Exception {
        Long id = 11L;

        Cidade cidade = Cidade.builder()
                .id(id)
                .nome(criarNovaCidadeDto().getNome())
                .estado(criarNovaCidadeDto().getEstado())
                .build();

        BDDMockito.given(service.getById(id)).willReturn(Optional.of(cidade));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CIDADE_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(11L))
                .andExpect(jsonPath("nome").value(criarNovaCidadeDto().getNome()))
                .andExpect(jsonPath("estado").value(criarNovaCidadeDto().getEstado()));
    }

    @Test
    @DisplayName("Deve retornar resource not found quando a cidade procurada não existir.")
    public void cityNotFoundTest() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CIDADE_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar uma cidade.")
    public void deleteCityTest() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.of(Cidade.builder().id(11L).build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CIDADE_API.concat("/" + 11))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar resource not found quando não encontar uma cidade para deletar.")
    public void deleteInexistentCityTest() throws Exception {

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CIDADE_API.concat("/" + 11))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar uma cidade.")
    public void updateCityTest() throws Exception {
        Long id = 11L;
        String json = new ObjectMapper().writeValueAsString(criarNovaCidadeDto());

        Cidade cidadeAtualizando = Cidade.builder().id(id).nome("Campo Grande").estado("MS").build();
        BDDMockito.given(service.getById(anyLong()))
                .willReturn(Optional.of(cidadeAtualizando));

        Cidade cidadeAtualizar = Cidade.builder().id(id).nome("Campo Grande").estado("MS").build();
        BDDMockito.given(service.update(cidadeAtualizando))
                .willReturn(cidadeAtualizar);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CIDADE_API.concat("/" + id))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("nome").value(criarNovaCidadeDto().getNome()))
                .andExpect(jsonPath("estado").value(criarNovaCidadeDto().getEstado()));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar uma cidade inexistente.")
    public void updateInexistentCityTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(criarNovaCidadeDto());
        BDDMockito.given(service.getById(anyLong()))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CIDADE_API.concat("/" + 11))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar todas as cidades filtrado pelo nome.")
    public void findAllCitiesByNameTest() throws Exception {
        Cidade cidade = criarNovaCidade();

        BDDMockito.given(service.findAllByNome(anyString()))
                .willReturn(Collections.singletonList(cidade));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CIDADE_API.concat("/search"))
                .param("nome", "Campo")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar todas as cidades filtrado pelo estado.")
    public void findAllCitiesByStateTest() throws Exception {
        Cidade cidade = criarNovaCidade();

        BDDMockito.given(service.findAllByNome(anyString()))
                .willReturn(Collections.singletonList(cidade));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CIDADE_API.concat("/search"))
                .param("estado", "Mato GroSSo")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar um erro ao buscar cidades por nome ou estado sem passar os parâmetros.")
    public void findAllCitiesByStateOrNameErrorTest() throws Exception {
        Cidade cidade = criarNovaCidade();

        BDDMockito.given(service.findAllByNome(anyString()))
                .willReturn(Collections.singletonList(cidade));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CIDADE_API.concat("/search"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest());
    }

    private CidadeDTO criarNovaCidadeDto() {
        return CidadeDTO.builder()
                .nome("Campo Grande")
                .estado("MS")
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
