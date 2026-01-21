package br.com.academia.bibliotecalocacao.integracao;

import br.com.academia.bibliotecalocacao.dtos.response.LocatarioResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



import java.time.LocalDate;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LocatarioControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @DisplayName("Deve criar um locatário com sucesso via API")
    void deveCriarLocatarioComSucesso() throws Exception {
        // Arrange - Criando o Record de entrada
        LocatarioResponse request = new LocatarioResponse(
                null,
                "Carlos Integration",
                "Masculino",
                "11977776666",
                "carlos@teste.com",
                LocalDate.of(1995, 10, 20),
                "11122233344"
        );

        // Act & Assert
        mockMvc.perform(post("/api/locatarios")
                        .contentType(MediaType.APPLICATION_JSON) // Use o objeto, não a String _VALUE
                        .content(objectMapper.writeValueAsString(request))) // Serializa o Record
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Carlos Integration"))
                .andExpect(jsonPath("$.cpf").value("11122233344"));

    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar locatário inexistente")
    void deveRetornarErroAoBuscarInexistente() throws Exception {
        mockMvc.perform(get("/api/locatarios/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve listar todos os locatários cadastrados")
    void deveListarLocatarios() throws Exception {
        mockMvc.perform(get("/api/locatarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)));
    }

}
