package io.github.wendergalan.desafiocompassouol.api.resources;

import io.github.wendergalan.desafiocompassouol.api.resource.HomeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static io.github.wendergalan.desafiocompassouol.utility.Constants.PASSWORD;
import static io.github.wendergalan.desafiocompassouol.utility.Constants.USERNAME;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Deve redirecionar para a documentação do Swagger.")
    @WithMockUser(username = USERNAME, password = PASSWORD, roles = "ADMIN")
    public void redirectSwaggerTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("Deve dar não autorizado ao tentar acessar a API sem credenciais.")
    public void redirectSwaggerUnauthorizedTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isUnauthorized());
    }
}