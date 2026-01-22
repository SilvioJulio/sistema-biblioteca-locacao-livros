package br.com.academia.bibliotecalocacao.integracao; // Ajuste seu pacote


import br.com.academia.bibliotecalocacao.dtos.request.LivroRequest;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.academia.bibliotecalocacao.entity.Autor;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc // OBRIGATÓRIO para injetar o MockMvc corretamente
@Transactional
public class LivroCotrollerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;




    private Autor criarAutorPadrao() {
        Autor autor = new Autor();
        autor.setNome("J.R.R. Tolkien");
        autor.setCpf("11122233344");
        autor.setSexo("Masculino");
        autor.setAnoNascimento(1892);
        return autorRepository.save(autor);
    }


    @Test
    @DisplayName("Deve criar um livro com sucesso")
    void deveCriarUmLivroComSucesso() throws Exception {
        Autor autor = new Autor();
        autor.setNome("J.R.R. Tolkien");
        autor.setCpf("11122233344");
        autor.setSexo("Masculino");
        autor.setAnoNascimento(1892);
        autor = autorRepository.save(autor);

        LivroRequest request = new LivroRequest(
                "O Senhor dos Anéis",
                "9788533613379",
                LocalDate.of(1954, 7, 29),
                autor.getId()
        );

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Agora o .content será reconhecido
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("O Senhor dos Anéis"));
    }



    @Test
    @DisplayName("Deve listar livros com paginação")
    void deveListarLivros() throws Exception {
        // 1. Garante que existe ao menos um registro
        Autor autor = criarAutorPadrao();
        Livro livro = new Livro();
        livro.setNome("Livro Teste");
        livro.setIsbn("9788533613379");
        livro.setDataPublicacao(LocalDate.now());
        livro.setAutor(autor);
        livroRepository.saveAndFlush(livro); // Força a gravação no banco

        mockMvc.perform(get("/api/livros")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Livro Teste"));
    }

    @Test
    @DisplayName("Deve atualizar um livro com sucesso")
    void deveAtualizarLivro() throws Exception {
        // 1. PREPARAÇÃO: Criar um autor e um livro para serem atualizados
        Autor autor = criarAutorPadrao();

        Livro livroExistente = new Livro();
        livroExistente.setNome("Nome Antigo");
        livroExistente.setIsbn("9788533613379");
        livroExistente.setDataPublicacao(LocalDate.now());
        livroExistente.setAutor(autor);
        livroExistente = livroRepository.saveAndFlush(livroExistente);


        LivroRequest request = new LivroRequest(
                "Nome Atualizado",
                "9788533613379",
                LocalDate.now(),
                autor.getId()
        );

        // 3. EXECUÇÃO E VALIDAÇÃO
        mockMvc.perform(put("/api/livros/{id}", livroExistente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.id").value(livroExistente.getId()));
    }

    @Test
    @DisplayName("Deve deletar um livro com sucesso")
    void deveDeletarLivro() throws Exception {
        // 1. PREPARAÇÃO: Criar o livro que será excluído
        Autor autor = criarAutorPadrao();

        Livro livroParaDeletar = new Livro();
        livroParaDeletar.setNome("Livro Para Deletar");
        livroParaDeletar.setIsbn("1234567890123");
        livroParaDeletar.setDataPublicacao(LocalDate.now());
        livroParaDeletar.setAutor(autor);
        livroParaDeletar = livroRepository.saveAndFlush(livroParaDeletar);

        // 2. EXECUÇÃO
        mockMvc.perform(delete("/api/livros/{id}", livroParaDeletar.getId()))
                .andExpect(status().isNoContent());

        // 3. VALIDAÇÃO: Garantir que o registro não existe mais no banco
        boolean aindaExiste = livroRepository.existsById(livroParaDeletar.getId());
        assert !aindaExiste;
    }

    @Test
    @DisplayName("Deve deletar um livro com sucesso e retornar 204")
    void deveDeletarLivroComSucesso() throws Exception {
        // 1. PREPARAÇÃO (Arrange)
        Autor autor = criarAutorPadrao();

        Livro livroParaDeletar = new Livro();
        livroParaDeletar.setNome("Livro para Exclusão");
        livroParaDeletar.setIsbn("9780000000000");
        livroParaDeletar.setDataPublicacao(LocalDate.now());
        livroParaDeletar.setAutor(autor);

        // Salvamos no banco para ter o que deletar
        livroParaDeletar = livroRepository.saveAndFlush(livroParaDeletar);
        Long idParaDeletar = livroParaDeletar.getId();

        // 2. EXECUÇÃO (Act)
        mockMvc.perform(delete("/api/livros/{id}", idParaDeletar))

                // 3. VALIDAÇÃO (Assert)
                .andExpect(status().isNoContent()); // Status 204

        // Verificação final: O livro não deve mais existir no repositório
        boolean existeNoBanco = livroRepository.existsById(idParaDeletar);
        assert !existeNoBanco;
    }



}




