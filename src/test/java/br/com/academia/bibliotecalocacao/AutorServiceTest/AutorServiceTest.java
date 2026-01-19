package br.com.academia.bibliotecalocacao.AutorServiceTest;

import br.com.academia.bibliotecalocacao.dtos.response.AutorResponse;
import br.com.academia.bibliotecalocacao.entity.Autor;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import br.com.academia.bibliotecalocacao.service.AutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    @InjectMocks
    private AutorService autorService;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void deveCriarAutorComSucessoTest() {
        AutorResponse autorResponse = new AutorResponse(null, "João Silva", "Masculino", 1983, "04245678960");
        Autor autorEntity = new Autor();
        autorEntity.setNome("João Silva");
        autorEntity.setCpf("04245678960");
        autorEntity.setAnoNascimento(1983);


        when(autorRepository.save(org.mockito.ArgumentMatchers.any(Autor.class))).thenReturn(autorEntity);

        AutorResponse resultado = autorService.criarAutor(autorResponse);

        assertNotNull(resultado);
        assertEquals(autorResponse.nome(), resultado.nome());
        verify(autorRepository, times(1)).save(org.mockito.ArgumentMatchers.any(Autor.class));
    }

    @Test
    void deveLancarExcecaoQuandoAutorNaoEncontradoPorId() {

        Long autorId = 1L;


        when(autorRepository.findById(autorId)).thenReturn(Optional.empty());


        RuntimeException excecao = assertThrows(RuntimeException.class, () -> autorService.buscarAutorPorId(autorId));


        assertEquals("Autor não encontrado com ID: " + autorId, excecao.getMessage());

        verify(autorRepository, times(1)).findById(autorId);
    }

    @Test
    void deveBuscarAutorPorIdComSucesso() {
        Long autorId = 1L;


        Autor autorEntity = new Autor();
        autorEntity.setId(autorId);
        autorEntity.setNome("João Silva");

        when(autorRepository.findById(autorId)).thenReturn(Optional.of(autorEntity));

        AutorResponse resultado = autorService.buscarAutorPorId(autorId);

        assertNotNull(resultado);
        assertEquals(autorId, resultado.id());
        assertEquals("João Silva", resultado.nome());

        verify(autorRepository, times(1)).findById(autorId);

    }

    @Test
    void deveExcepoLancarQuandoNaoHouverAutoresCadastradosComSucesso(){
        Pageable pageable = PageRequest.of(0, 10);

        when(autorRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<AutorResponse> resultado = autorService.listarTodosAutores(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(autorRepository, times(1)).findAll(pageable);
    }


    @Test
    void deveListarTodosAutoresComSucesso() {

        Autor autorEntity = new Autor();
        autorEntity.setId(1L);

        Page<Autor> pageAutores = new PageImpl<>(java.util.List.of(autorEntity));
        Pageable pageable = PageRequest.of(0, 10);

        when(autorRepository.findAll(pageable)).thenReturn(pageAutores);


        Page<AutorResponse> resultado = autorService.listarTodosAutores(pageable);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());

        verify(autorRepository, times(1)).findAll(pageable);

    }

    @Test
    void deveAtualizarAutorComSucessoTest() {
        Long autorId = 1L;

        AutorResponse autorResponseAtualizado = new AutorResponse(null, "Maria Souza", "Feminino", 1990, "12345678901");

        Autor autorExistente = new Autor();
        autorExistente.setId(autorId);
        autorExistente.setNome("João Silva");
        autorExistente.setCpf("04245678960");
        autorExistente.setAnoNascimento(1983);

        when(autorRepository.findById(autorId)).thenReturn(Optional.of(autorExistente));
        when(autorRepository.save(org.mockito.ArgumentMatchers.any(Autor.class))).thenReturn(autorExistente);
        AutorResponse resultado = autorService.atualizarAutor(autorId, autorResponseAtualizado);
        assertNotNull(resultado);
        assertEquals(autorResponseAtualizado.nome(), resultado.nome());
        verify(autorRepository, times(1)).findById(autorId);
        verify(autorRepository, times(1)).save(org.mockito.ArgumentMatchers.any(Autor.class));
    }



    @Test
    void deveDeletarAutorComSucesso() {

        // Preparação
        Long autorId = 1L;
        Autor autorExistente = new Autor();
        autorExistente.setId(autorId);

        // Configuramos os mocks
        when(autorRepository.findById(autorId)).thenReturn(Optional.of(autorExistente));
        when(livroRepository.existsByAutorId(autorId)).thenReturn(false);

        // (Ação)
        autorService.deletarAutor(autorId);

        // Assert (Verificação)
        verify(autorRepository, times(1)).findById(autorId);
        verify(livroRepository, times(1)).existsByAutorId(autorId);

        // Verifique se o seu service usa delete() ou deleteById()
        // Se o service usa repository.delete(autor), mantenha assim:
        verify(autorRepository, times(1)).delete(autorExistente);
    }
}

