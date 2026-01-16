package br.com.academia.bibliotecalocacao;

import br.com.academia.bibliotecalocacao.dtos.AluguelDTO;
import br.com.academia.bibliotecalocacao.entity.Aluguel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.academia.bibliotecalocacao.dtos.AluguelRequestDTO;
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


import java.time.LocalDate;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class serviveAluguelTest {

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

        AluguelRequestDTO aluguelRequestDTO = new AluguelRequestDTO(1L, 1L);

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("Java Progressivo 2026");

        Locatario locatario = new Locatario();
        locatario.setId(1L);
        locatario.setNome("Maria Silva");

        // Mocking the repository methods
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));

        when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(invocation -> {
            Aluguel a = invocation.getArgument(0);
            a.setId(10L); // Simula o ID gerado pelo PostgreSQL no momento do save
            return a;
        });

        AluguelDTO resultado = aluguelService.criar(aluguelRequestDTO);

        assertNotNull(resultado, "O resultado não deve ser nulo");
        assertEquals(10L, resultado.id());
        assertEquals("Java Progressivo 2026", resultado.livroNome());
        assertEquals("Maria Silva", resultado.locatarioNome());

        // Validação das Datas (Regra de Negócio)
        LocalDate dataAtual = LocalDate.now();
        assertEquals(dataAtual, resultado.dataRetirada(), "A data de retirada deve ser a data atual");
        assertEquals(dataAtual.plusDays(2), resultado.dataDevolucao(), "A data de devolução deve ser 2 dias após a data de retirada");

        verify(aluguelRepository, times(1)).save(any(Aluguel.class));


    }
}
