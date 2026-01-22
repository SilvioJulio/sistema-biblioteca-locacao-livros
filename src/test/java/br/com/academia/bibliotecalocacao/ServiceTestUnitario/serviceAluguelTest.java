package br.com.academia.bibliotecalocacao.ServiceTestUnitario;

import br.com.academia.bibliotecalocacao.dtos.response.AluguelResponse;
import br.com.academia.bibliotecalocacao.entity.Aluguel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.academia.bibliotecalocacao.dtos.request.AluguelRequest;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.repository.AluguelRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import br.com.academia.bibliotecalocacao.service.AluguelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class serviceAluguelTest {

    @InjectMocks
    private AluguelService aluguelService;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private LocatarioRepository locatarioRepository;

    @Mock
    private AluguelRepository aluguelRepository;


    @Test
    void deveCriarAluguelComSucessoTest() {
        AluguelRequest aluguelRequest = new AluguelRequest(1L, 1L, 5);
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("Java Progressivo 2026");

        Locatario locatario = new Locatario();
        locatario.setId(1L);
        locatario.setNome("Maria Silva");

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));
        when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(invocation -> {
            Aluguel a = invocation.getArgument(0);
            a.setId(10L);
            return a;
        });

        AluguelResponse resultado = aluguelService.criar(aluguelRequest);

        assertNotNull(resultado);
        assertEquals(10L, resultado.id());
        verify(aluguelRepository, times(1)).save(any(Aluguel.class));
    }

    @Test
    void deveRetornarErroAoCriarAluguelComLivroInexistenteTest() {
        AluguelRequest aluguelRequest = new AluguelRequest(999L, 1L, 5);
        when(livroRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                aluguelService.criar(aluguelRequest)
        );

        assertEquals("Livro não encontrado com ID: 999", exception.getMessage());
        verify(livroRepository).findById(999L);
        verify(aluguelRepository, never()).save(any());
    }

    @Test
    void deveLancarErroAoBuscarAluguelInexistenteTest() {
        Long aluguelId = 999L;
        when(aluguelRepository.findById(aluguelId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                aluguelService.buscarPorId(aluguelId)
        );

        assertEquals("Aluguel não encontrado com ID: " + aluguelId, exception.getMessage());
        verify(aluguelRepository).findById(aluguelId);
    }

    @Test
    void deveDeletarUmAluguelComSucesso() {
        Long aluguelId = 1L;
        Aluguel aluguel = new Aluguel();
        when(aluguelRepository.findById(aluguelId)).thenReturn(Optional.of(aluguel));

        aluguelService.deletar(aluguelId);

        verify(aluguelRepository).delete(aluguel);
    }

    @Test
    void deveLancarExcecaoAoDeletarIdInexistente() {
        // Arrange
        Long idInexistente = 700L;
        // Simulamos que o repositório retorna vazio
        when(aluguelRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                aluguelService.deletar(idInexistente)
        );

        assertEquals("Aluguel não encontrado com ID: " + idInexistente, exception.getMessage());

        // Verifica que tentou buscar, mas NUNCA chamou o delete
        verify(aluguelRepository).findById(idInexistente);
        verify(aluguelRepository, never()).delete(any());
    }

    @Test
    void deveListarTodosOsAlugueisComSucesso() {
        PageRequest pageable = PageRequest.of(0, 10);
        Aluguel aluguel = new Aluguel();
        aluguel.setId(1L);
        aluguel.setLivro(new Livro());
        aluguel.setLocatario(new Locatario());

        Page<Aluguel> pageFake = new PageImpl<>(List.of(aluguel));
        when(aluguelRepository.findAll(pageable)).thenReturn(pageFake);

        Page<AluguelResponse> resultado = aluguelService.listarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(aluguelRepository).findAll(pageable);
    }
}



