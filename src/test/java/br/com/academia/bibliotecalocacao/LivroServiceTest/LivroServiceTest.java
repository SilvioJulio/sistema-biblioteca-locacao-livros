package br.com.academia.bibliotecalocacao.LivroServiceTest;


import br.com.academia.bibliotecalocacao.dtos.request.LivroRequest;
import br.com.academia.bibliotecalocacao.dtos.response.AutorResponse;
import br.com.academia.bibliotecalocacao.dtos.response.LivroResponse;
import br.com.academia.bibliotecalocacao.entity.Autor;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.mapper.LivroMapper;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import br.com.academia.bibliotecalocacao.service.LivroService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private LivroMapper livroMapper; // Deve ser um Mock

    @InjectMocks
    private LivroService livroService;


    @Test
    void deveCriarLivroComSucessoTest() {
        // Arrange
        Long autorId = 1L;
        LocalDate dataPublicacao = LocalDate.of(1954, 7, 29);

        // Request de entrada
        LivroRequest request= new LivroRequest  (
                "O Senhor dos Anéis",
                "9788533613409",
                dataPublicacao,
                autorId
        );

        // Entidade Autor existente
        Autor autor = new Autor();
        autor.setId(autorId);
        autor.setNome("J.R.R. Tolkien");

        // Entidade Livro (montada pelo mapper a partir do request)
        Livro entidade = new Livro();
        entidade.setNome("O Senhor dos Anéis");
        entidade.setIsbn("9788533613409");
        entidade.setDataPublicacao(dataPublicacao);
        // o autor será setado no service após buscar no repositório

        // Entidade salva (com ID gerado e autor setado)
        Livro salvo = new Livro();
        salvo.setId(10L);
        salvo.setNome("O Senhor dos Anéis");
        salvo.setIsbn("9788533613409");
        salvo.setDataPublicacao(dataPublicacao);
        salvo.setAutor(autor);

        // Response esperado
        LivroResponse responseEsperado = new LivroResponse(
                10L,
                "O Senhor dos Anéis",
                "9788533613409",
                dataPublicacao,
                new AutorResponse(autorId, "J.R.R. Tolkien", null, null, null)
        );

        // Stubs
        when(livroRepository.existsByIsbn("9788533613409")).thenReturn(false);
        when(livroMapper.toEntity(request)).thenReturn(entidade);
        when(autorRepository.findById(autorId)).thenReturn(Optional.of(autor));
        when(livroRepository.save(entidade)).thenReturn(salvo);
        when(livroMapper.toResponse(salvo)).thenReturn(responseEsperado);

        // Act
        LivroResponse result = livroService.criarLivro(request);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals("O Senhor dos Anéis", result.nome());
        assertEquals("9788533613409", result.isbn());
        assertNotNull(result.autor());
        assertEquals(autorId, result.autor().id());
        assertEquals(dataPublicacao, result.dataPublicacao());

        // Verificações de interação
        verify(livroRepository).existsByIsbn("9788533613409");
        verify(livroMapper).toEntity(request);
        verify(autorRepository).findById(autorId);
        verify(livroRepository).save(entidade);
        verify(livroMapper).toResponse(salvo);
        verifyNoMoreInteractions(livroRepository, autorRepository, livroMapper);
    }

    // ----------- TESTES EXTRAS ÚTEIS (bônus) -----------

    @Test
    void naoDeveCriarLivro_quandoIsbnJaExistir() {
        // Arrange
        LivroRequest request = new LivroRequest(
                "Outro Livro",
                "1234567890X",
                LocalDate.of(2000, 1, 1),
                5L
        );

        when(livroRepository.existsByIsbn("1234567890X")).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> livroService.criarLivro(request));
        assertEquals("ISBN já cadastrado", ex.getMessage());

        verify(livroRepository).existsByIsbn("1234567890X");
        verifyNoMoreInteractions(livroRepository);
        verifyNoInteractions(autorRepository, livroMapper);
    }

    @Test
    void naoDeveCriarLivro_quandoAutorNaoEncontrado() {
        // Arrange
        Long autorId = 999L;
        LivroRequest request = new LivroRequest(
                "Livro Sem Autor",
                "9780000000002",
                LocalDate.of(2020, 5, 10),
                autorId
        );

        Livro entidade = new Livro();
        entidade.setNome("Livro Sem Autor");
        entidade.setIsbn("9780000000002");
        entidade.setDataPublicacao(LocalDate.of(2020, 5, 10));

        when(livroRepository.existsByIsbn("9780000000002")).thenReturn(false);
        when(livroMapper.toEntity(request)).thenReturn(entidade);
        when(autorRepository.findById(autorId)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> livroService.criarLivro(request));
        assertTrue(ex.getMessage().contains("Autor não encontrado"));

        verify(livroRepository).existsByIsbn("9780000000002");
        verify(livroMapper).toEntity(request);

    }
    @Test
    void deveBuscarLivroPorIdComSucessoTest() {
        // Arrange
        Long livroId = 1L;

        Autor autor = new Autor();
        autor.setId(2L);
        autor.setNome("Autor Exemplo");

        Livro livro = new Livro();
        livro.setId(livroId);
        livro.setNome("Livro Exemplo");
        livro.setIsbn("9781234567890");
        livro.setDataPublicacao(LocalDate.of(2021, 1, 1));
        livro.setAutor(autor);

        LivroResponse esperadoResponse = new LivroResponse(
                livroId,
                "Livro Exemplo",
                "9781234567890",
                LocalDate.of(2021, 1, 1),
                new AutorResponse(2L, "Autor Exemplo", null, null, null)
        );

        when(livroRepository.findById(livroId)).thenReturn(Optional.of(livro));
        when(livroMapper.toResponse(livro)).thenReturn(esperadoResponse);

        // Act
        LivroResponse result = livroService.buscarLivroPorId(livroId);

        // Assert
        assertNotNull(result);
        assertEquals(esperadoResponse.id(), result.id());
        assertEquals(esperadoResponse.nome(), result.nome());
        assertEquals(esperadoResponse.isbn(), result.isbn());
        assertEquals(esperadoResponse.dataPublicacao(), result.dataPublicacao());
        assertNotNull(result.autor());
        assertEquals(esperadoResponse.autor().id(), result.autor().id());
        assertEquals(esperadoResponse.autor().nome(), result.autor().nome());

        verify(livroRepository).findById(livroId);
        verify(livroMapper).toResponse(livro);
        verifyNoMoreInteractions(livroRepository, livroMapper);
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoEncontrado() {
        // Arrange
        Long livroId = 99L;

        when(livroRepository.findById(livroId)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> livroService.buscarLivroPorId(livroId));
        assertEquals("Livro não encontrado", ex.getMessage());

        verify(livroRepository).findById(livroId);
        verifyNoMoreInteractions(livroRepository);
        verifyNoInteractions(livroMapper);
    }

    @Test
    void deveAtualizarLivroComSucessoTest() {
        // Arrange
        Long livroId = 1L;
        Long autorId = 2L;
        LocalDate dataPublicacao = LocalDate.of(2000, 1, 1);

        LivroRequest request = new LivroRequest(
                "Livro Atualizado",
                "9781234567890",
                dataPublicacao,
                autorId
        );

        Autor autor = new Autor();
        autor.setId(autorId);
        autor.setNome("Autor Atualizado");

        Livro existente = new Livro();
        existente.setId(livroId);
        existente.setNome("Livro Antigo");
        existente.setIsbn("9780987654321");
        existente.setDataPublicacao(LocalDate.of(1990, 1, 1));

        Livro atualizado = new Livro();
        atualizado.setId(livroId);
        atualizado.setNome("Livro Atualizado");
        atualizado.setIsbn("9781234567890");
        atualizado.setDataPublicacao(dataPublicacao);
        atualizado.setAutor(autor);

        LivroResponse esperadoResponse = new LivroResponse(
                livroId,
                "Livro Atualizado",
                "9781234567890",
                dataPublicacao,
                new AutorResponse(autorId, "Autor Atualizado", null, null, null)
        );

        when(livroRepository.findById(livroId)).thenReturn(Optional.of(existente));
        when(livroRepository.existsByIsbn("9781234567890")).thenReturn(false);
        when(autorRepository.findById(autorId)).thenReturn(Optional.of(autor));
        when(livroRepository.save(existente)).thenReturn(atualizado);
        when(livroMapper.toResponse(atualizado)).thenReturn(esperadoResponse);

        // Act
        LivroResponse result = livroService.atualizarLivro(livroId, request);

        // Assert
        assertNotNull(result);
        assertEquals(esperadoResponse.id(), result.id());
        assertEquals(esperadoResponse.nome(), result.nome());
        assertEquals(esperadoResponse.isbn(), result.isbn());
        assertEquals(esperadoResponse.dataPublicacao(), result.dataPublicacao());
        assertNotNull(result.autor());
        assertEquals(esperadoResponse.autor().id(), result.autor().id());
        assertEquals(esperadoResponse.autor().nome(), result.autor().nome());
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarLivroNaoEncontrado() {
        // Arrange
        Long livroId = 99L;

        LivroRequest request = new LivroRequest(
                "Livro Inexistente",
                "9780000000000",
                LocalDate.of(2020, 1, 1),
                1L
        );

        when(livroRepository.findById(livroId)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> livroService.atualizarLivro(livroId, request));
        assertEquals("Livro não encontrado", ex.getMessage());

        verify(livroRepository).findById(livroId);
        verifyNoMoreInteractions(livroRepository);
        verifyNoInteractions(autorRepository, livroMapper);
    }
    @Test
    void deveLisatarTodosLivrosComSucessoTest() {
        // Arrange
        Livro livro1 = new Livro();
        livro1.setId(1L);
        livro1.setNome("Livro 1");
        livro1.setIsbn("9781111111111");
        livro1.setDataPublicacao(LocalDate.of(2020, 1, 1));

        Livro livro2 = new Livro();
        livro2.setId(2L);
        livro2.setNome("Livro 2");
        livro2.setIsbn("9782222222222");
        livro2.setDataPublicacao(LocalDate.of(2021, 2, 2));

        LivroResponse response1 = new LivroResponse(
                1L,
                "Livro 1",
                "9781111111111",
                LocalDate.of(2020, 1, 1),
                null
        );

        LivroResponse response2 = new LivroResponse(
                2L,
                "Livro 2",
                "9782222222222",
                LocalDate.of(2021, 2, 2),
                null
        );

        when(livroRepository.findAll()).thenReturn(java.util.List.of(livro1, livro2));
        when(livroMapper.toResponse(livro1)).thenReturn(response1);
        when(livroMapper.toResponse(livro2)).thenReturn(response2);

        // Act
        java.util.List<LivroResponse> result = livroService.listarTodosLivros();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(livroRepository).findAll();
        verify(livroMapper).toResponse(livro1);
        verify(livroMapper).toResponse(livro2);
        verifyNoMoreInteractions(livroRepository, livroMapper);
    }

    @Test
    void deveDeletarLivroComSucessoTest() {
        // Arrange
        Long livroId = 1L;

        when(livroRepository.existsById(livroId)).thenReturn(true);

        // Act
        livroService.deletarLivroPorId(livroId);

        // Assert
        verify(livroRepository).existsById(livroId);
        verify(livroRepository).deleteById(livroId);
        verifyNoMoreInteractions(livroRepository);
    }
}