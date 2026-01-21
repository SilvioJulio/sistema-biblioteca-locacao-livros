package br.com.academia.bibliotecalocacao.AluguelServiceTest;

import br.com.academia.bibliotecalocacao.dtos.response.AluguelResponse;
import br.com.academia.bibliotecalocacao.entity.Aluguel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.academia.bibliotecalocacao.dtos.request.AluguelRequest;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.repository.AluguelRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import br.com.academia.bibliotecalocacao.service.AluguelService;
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


import java.time.LocalDate;
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

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void deveCriarAluguelComSucessoTest() {

        AluguelRequest aluguelRequest = new AluguelRequest(1L, 1L);

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

        AluguelResponse resultado = aluguelService.criar(aluguelRequest);

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

    @Test
    void deveRetornarErroAoCriarAluguelComLivroInexistenteTest() {
        AluguelRequest aluguelRequest = new AluguelRequest(999L, 1L);

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("Java Progressivo 2026");

        when(livroRepository.findById(999L)).thenReturn(Optional.empty());
        try {
            aluguelService.criar(aluguelRequest);
        } catch (RuntimeException e) {
            assertEquals("Livro não encontrado com ID: " + aluguelRequest.id(), e.getMessage());
        }
    }
    @Test
    void  deveAtualizarAluguelComSucessoTest() {
        Long aluguelId = 10L;

        AluguelRequest aluguelRequest = new AluguelRequest(1L, 1L);

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("Java Progressivo 2026");

        Locatario locatario = new Locatario();
        locatario.setId(1L);
        locatario.setNome("Maria Silva");

        Aluguel aluguelExistente = new Aluguel();
        aluguelExistente.setId(aluguelId);
        aluguelExistente.setLivro(livro);
        aluguelExistente.setLocatario(locatario);
        aluguelExistente.setDataRetirada(LocalDate.now());
        aluguelExistente.setDataDevolucao(LocalDate.now().plusDays(2));

        when(aluguelRepository.findById(aluguelId)).thenReturn(Optional.of(aluguelExistente));
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));
        when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AluguelResponse resultado = aluguelService.atualizar(aluguelId, aluguelRequest);

        assertNotNull(resultado, "O resultado não deve ser nulo");
        assertEquals(aluguelId, resultado.id());
        assertEquals("Java Progressivo 2026", resultado.livroNome());
        assertEquals("Maria Silva", resultado.locatarioNome());

        verify(aluguelRepository, times(1)).save(any(Aluguel.class));
    }

    @Test
    void deveLancarErroAoBuscarAluguelInexistenteTest() {
        Long aluguelId = 999L;

        when(aluguelRepository.findById(aluguelId)).thenReturn(Optional.empty());

        try {
            aluguelService.buscarPorId(aluguelId);
        } catch (RuntimeException e) {
            assertEquals("Aluguel não encontrado com ID: " + aluguelId, e.getMessage());
        }

        verify(aluguelRepository, times(1)).findById(aluguelId);
    }

    @Test
    void deveLisarTodosOsAlugueisComSucesso() {

        // Preparando dados de teste
        PageRequest pageable = PageRequest.of(0, 10);
        Aluguel aluguel1 = new Aluguel();
        aluguel1.setId(1L);

        aluguel1.setLivro(new Livro());
        aluguel1.setLocatario(new Locatario());
        aluguel1.setDataRetirada(LocalDate.now());
        aluguel1.setDataDevolucao(LocalDate.now().plusDays(2));

        //Criando uma pagina fake
        Page<Aluguel> pageFake = new PageImpl<>(List.of(aluguel1));

        //Mockando o comportamento do repositorio
        when(aluguelRepository.findAll(pageable)).thenReturn(pageFake);

        //Chamando o servico
        Page<AluguelResponse> resultado = aluguelService.listarTodos(pageable);

        assertNotNull(resultado, "O resultado não deve ser nulo");
        assertEquals(1, resultado.getTotalElements(), "Deve conter 1 elemento na página");
        assertEquals(1L, resultado.getContent().get(0).id(), "O ID do aluguel deve ser 1L");

        verify(aluguelRepository, times(1)).findAll(pageable);
    }


    @Test
    void deveBuscarAluguelPorIdComSucessoTest() {
        Long aluguelId = 10L;

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("Java Progressivo 2026");

        Locatario locatario = new Locatario();
        locatario.setId(1L);
        locatario.setNome("Maria Silva");

        Aluguel aluguel = new Aluguel();
        aluguel.setId(aluguelId);
        aluguel.setLivro(livro);
        aluguel.setLocatario(locatario);
        aluguel.setDataRetirada(LocalDate.now());
        aluguel.setDataDevolucao(LocalDate.now().plusDays(2));

        when(aluguelRepository.findById(aluguelId)).thenReturn(Optional.of(aluguel));

        AluguelResponse resultado = aluguelService.buscarPorId(aluguelId);

        assertNotNull(resultado, "O resultado não deve ser nulo");
        assertEquals(aluguelId, resultado.id());
        assertEquals("Java Progressivo 2026", resultado.livroNome());
        assertEquals("Maria Silva", resultado.locatarioNome());

        verify(aluguelRepository, times(1)).findById(aluguelId);
    }

    @Test
    void deveDeletarUmAluguelComSucesso() {
        // 1. Arrange (Preparar)
        Long aluguelId = 1l;
        Aluguel aluguel = new Aluguel();

        // Configura o mock para encontrar o aluguel
        when(aluguelRepository.findById(aluguelId)).thenReturn(Optional.of(aluguel));

        aluguelService.deletar(aluguelId);

        verify(aluguelRepository, times(1)).delete(aluguel);

    }

    @Test
    void deveLancarExcecaoAoDeletarIdInexistente() {
        Long idInexistente = 700L;

        Aluguel aluguel = new Aluguel();

        when(aluguelRepository.findById(idInexistente)).thenReturn(Optional.of(aluguel));

        aluguelService.deletar(idInexistente);

        verify(aluguelRepository, times(1)).delete(aluguel);
    }

}
