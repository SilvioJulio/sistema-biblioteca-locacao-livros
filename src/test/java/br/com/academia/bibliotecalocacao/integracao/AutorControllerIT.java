package br.com.academia.bibliotecalocacao.integracao;


import br.com.academia.bibliotecalocacao.dtos.request.AutorRequest;
import br.com.academia.bibliotecalocacao.entity.Autor;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AutorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;



    @BeforeEach
    void setUp() {
        autorRepository.deleteAll();
    }



    @Test
    @DisplayName("Deve criar um autor com sucesso")
    void deveCriarUmAutorComSucesso() throws Exception {
        // Gera um final aleatório para o CPF para evitar duplicidade
        String cpfUnico = "12345678" + (int)(Math.random() * 999);
        if (cpfUnico.length() > 11) cpfUnico = cpfUnico.substring(0, 11);

        AutorRequest request = new AutorRequest(
                "Fernanda Silva",
                "Feminino",
                1989,
                cpfUnico, // CPF dinâmico
                null
        );

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cpf").value(cpfUnico));
    }


    @Test
    @DisplayName("Deve encontrar autor por ID")
    void deveBuscarAutorPorId() throws Exception {

        Autor autor = new Autor();
        autor.setNome("J.R.R. Tolkien");
        autor.setSexo("Femenino");
        autor.setAnoNascimento(1989);
        autor.setCpf("87945612312");

        Autor autorSalvo = autorRepository.save(autor);

        mockMvc.perform(get("/api/autores/{id}", autorSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera status 200 OK
                .andExpect(jsonPath("$.id").value(autorSalvo.getId()))
                .andExpect(jsonPath("$.nome").value("J.R.R. Tolkien"))
                .andExpect(jsonPath("$.cpf").value("87945612312"));
    }

    @Test
    @DisplayName("Deve atualizar o CPF de um autor com sucesso")
    void deveAtualizarCpfDoAutor() throws Exception {

        Autor autorOriginal = new Autor();
        autorOriginal.setNome("J.R.R. Tolkien");
        autorOriginal.setCpf("11122233344");
        autorOriginal.setAnoNascimento(1892);
        autorOriginal = autorRepository.save(autorOriginal);


        AutorRequest updateRequest = new AutorRequest(
                "J.R.R. Tolkien",
                "Masculino",
                1892,
                "99988877766", // NOVO CPF
                null
        );

        mockMvc.perform(put("/api/autores/{id}", autorOriginal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk()) // Ou isNoContent() dependendo do seu Controller
                .andExpect(jsonPath("$.cpf").value("99988877766")); // Valida o novo valor

        // 4. Assert - Opcional: Validar direto no banco de dados
        Autor autorNoBanco = autorRepository.findById(autorOriginal.getId()).get();
        assertEquals("99988877766", autorNoBanco.getCpf());
    }

    @Test
    @DisplayName("Deve listar todos os autores cadastrados com paginação")
    void deveListarTodosOsAutores() throws Exception {
        // 1. Arrange - Salvar autores
        Autor autor1 = new Autor(null, "Machado de Assis", "Masculino", 1839, "11122233344", null);
        Autor autor2 = new Autor(null, "Clarice Lispector", "Feminino", 1920, "55566677788", null);
        autorRepository.saveAll(List.of(autor1, autor2));

        // 2. Act & Assert
        mockMvc.perform(get("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].nome").value("Machado de Assis"))
                .andExpect(jsonPath("$.content[1].nome").value("Clarice Lispector"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.number").value(0)); // Página atual
    }


    @Test
    @DisplayName("Deve deletar um autor com sucesso")
    void deveDeletarAutor() throws Exception {
        // 1. Arrange - Salvar um autor para deletar
        Autor autor = new Autor(null, "Autor para Deletar", "Masculino", 1990, "99988877711", null);
        Autor autorSalvo = autorRepository.save(autor);

        // 2. Act - Chamada DELETE
        mockMvc.perform(delete("/api/autores/{id}", autorSalvo.getId()))
                .andExpect(status().isNoContent()); // Geralmente DELETE retorna 204 No Content

        boolean existeNoBanco = autorRepository.existsById(autorSalvo.getId());
        assertFalse(existeNoBanco, "O autor ainda existe no banco de dados");
    }




}


