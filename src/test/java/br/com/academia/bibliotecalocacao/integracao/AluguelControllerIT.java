package br.com.academia.bibliotecalocacao.integracao;


import br.com.academia.bibliotecalocacao.controller.AluguelController;
import br.com.academia.bibliotecalocacao.dtos.request.AluguelRequest;
import br.com.academia.bibliotecalocacao.entity.Aluguel;
import br.com.academia.bibliotecalocacao.entity.Autor;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.repository.AluguelRepository;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest // Essencial para carregar o AluguelService e seus Repositories
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AluguelControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LocatarioRepository locatarioRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AluguelRepository aluguelRepository;


    @BeforeEach
    void cleanDatabase() {
        aluguelRepository.deleteAll();
        livroRepository.deleteAll();
        autorRepository.deleteAll();
        locatarioRepository.deleteAll();
    }


    private Aluguel criarCenarioPadraoNoBanco() {
        Autor autor = new Autor();
        autor.setNome("J.R.R. Tolkien");
        autor.setSexo("Masculino");
        autor.setAnoNascimento(1892);
        autor.setCpf("12345678901");
        autor = autorRepository.save(autor);

        Livro livro = new Livro();
        livro.setNome("O Senhor dos Anéis");
        livro.setIsbn("ISBN-" + System.currentTimeMillis());
        livro.setDataPublicacao(LocalDate.of(1954, 7, 29));
        livro.setAutor(autor);
        livro = livroRepository.save(livro);

        Locatario locatario = new Locatario();
        locatario.setNome("João Silva");
        locatario.setCpf("98765432100");
        locatario.setEmail("joao@email.com");
        locatario.setTelefone("11999998888");
        locatario.setDataNascimento(LocalDate.of(1990, 1, 1));
        locatario = locatarioRepository.save(locatario);

        Aluguel aluguel = new Aluguel();
        aluguel.setLivro(livro);
        aluguel.setLocatario(locatario);

        return aluguelRepository.save(aluguel);
    }


    @Test
    @DisplayName("Deve criar um aluguel com sucesso")
    void deveCriarAluguel() throws Exception {

        Autor autor = new Autor();
        autor.setNome("Autor Nome");
        autor.setSexo("Masculino");
        autor.setAnoNascimento(1984);
        autor.setCpf("15975345689");
        autor = autorRepository.save(autor);

        Livro livro = new Livro();
        livro.setNome("Livro Teste");
        livro.setIsbn("ISBN-" + System.currentTimeMillis()); // Garante unicidade
        livro.setDataPublicacao(LocalDate.now());
        livro.setAutor(autor);
        livro = livroRepository.save(livro);

        Locatario locatario = new Locatario();
        locatario.setNome("Locatario Teste");
        locatario.setCpf("11122233344");
        locatario.setEmail("test@test.com");
        locatario.setTelefone("11912341234");
        locatario.setDataNascimento(LocalDate.now());
        locatario = locatarioRepository.save(locatario);

        // 4. Montar o Request (Certifique-se que o AluguelRequest mapeia corretamente os IDs)
        var request = new AluguelRequest(livro.getId(), locatario.getId(), 7);

        mockMvc.perform(post("/api/alugueis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }


    @Test
    @DisplayName("Deve buscar aluguel por ID")
    void deveBuscarPorId() throws Exception {
        // Organização (Arrange)
        Aluguel aluguel = criarCenarioPadraoNoBanco();

        // Ação e Validação (Act & Assert)
        mockMvc.perform(get("/api/alugueis/{id}", aluguel.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(aluguel.getId()));
    }


    @Test
    @DisplayName("Deve atualizar um aluguel")
    void deveAtualizarAluguel() throws Exception {
        Aluguel aluguelExistente = criarCenarioPadraoNoBanco();

        // Criar request com novos dados (usando os mesmos IDs de livro/locatario ou novos se quiser)
        var request = new AluguelRequest(aluguelExistente.getLivro().getId(),
                aluguelExistente.getLocatario().getId(), 15);

        mockMvc.perform(put("/api/alugueis/{id}", aluguelExistente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve listar todos com paginação")
    void deveListarTodos() throws Exception {
        criarCenarioPadraoNoBanco();

        mockMvc.perform(get("/api/alugueis")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Deve deletar um aluguel")
    void deveDeletarAluguel() throws Exception {
        Aluguel aluguel = criarCenarioPadraoNoBanco();

        mockMvc.perform(delete("/api/alugueis/{id}", aluguel.getId()))
                .andExpect(status().isNoContent());

        // Validação extra: verificar se realmente sumiu do banco
        assert aluguelRepository.findById(aluguel.getId()).isEmpty();
    }


}
