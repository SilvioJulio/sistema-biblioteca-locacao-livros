package br.com.academia.bibliotecalocacao.integracao;

import br.com.academia.bibliotecalocacao.dtos.request.LocatarioRequest;
import br.com.academia.bibliotecalocacao.dtos.response.LocatarioResponse;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;



import java.time.LocalDate;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LocatarioControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LocatarioRepository locatarioRepository;



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
    @DisplayName("Deve atualizar um locatário com sucesso quando os dados forem válidos")
    void deveAtualizarLocatariosComSucesso() throws Exception {
        // 1. Arrange - Salvar um locatário inicial no banco para ter o que atualizar
        Locatario locatarioOriginal = new Locatario();
        locatarioOriginal.setNome("Carlos Original");
        locatarioOriginal.setCpf("11122233344");
        locatarioOriginal.setTelefone("11977776666");
        locatarioOriginal.setEmail("carlos@velho.com");
        locatarioOriginal.setSexo("Masculino");
        locatarioOriginal.setDataNascimento(LocalDate.of(1995, 10, 20));

        locatarioOriginal = locatarioRepository.save(locatarioOriginal);

        // 2. Criar o REQUEST com os novos dados (Ex: mudando nome e e-mail)
        // Lembre-se: Use LocatarioRequest (entrada) e não o Response
        LocatarioRequest requestDeAtualizacao = new LocatarioRequest(
                "Carlos Alterado",   // Nome novo
                "11122233344",       // CPF (mantém o mesmo)
                "11988889999",       // Telefone novo
                "Masculino",
                "carlos.novo@teste.com", // E-mail novo
                LocalDate.of(1995, 10, 20)
        );

        // 3. Act - Executar o PUT para a URL /api/locatarios/{id}
        mockMvc.perform(put("/api/locatarios/{id}", locatarioOriginal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDeAtualizacao)))
                // 4. Assert - Validar se o status é OK e se os campos mudaram no JSON de retorno
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(locatarioOriginal.getId()))
                .andExpect(jsonPath("$.nome").value("Carlos Alterado"))
                .andExpect(jsonPath("$.email").value("carlos.novo@teste.com"))
                .andExpect(jsonPath("$.telefone").value("11988889999"));

        // Opcional: Validar persistência física no repositório
        var locatarioNoBanco = locatarioRepository.findById(locatarioOriginal.getId()).orElseThrow();
        assertEquals("Carlos Alterado", locatarioNoBanco.getNome());
    }




    @Test
    @DisplayName("Deve listar todos os locatários cadastrados")
    void deveListarLocatarios() throws Exception {
        mockMvc.perform(get("/api/locatarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)));
    }

}
