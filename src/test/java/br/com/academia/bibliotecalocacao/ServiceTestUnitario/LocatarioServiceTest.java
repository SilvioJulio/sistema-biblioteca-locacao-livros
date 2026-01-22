package br.com.academia.bibliotecalocacao.ServiceTestUnitario;

import br.com.academia.bibliotecalocacao.dtos.request.LocatarioRequest;
import br.com.academia.bibliotecalocacao.dtos.response.LocatarioResponse;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.mapper.LocatarioMapper;
import br.com.academia.bibliotecalocacao.repository.AluguelRepository;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import br.com.academia.bibliotecalocacao.service.LocatarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LocatarioServiceTest {

    @InjectMocks
    private LocatarioService locatarioService;

    @Mock
    private LocatarioRepository locatarioRepository;

    @Mock
    private LocatarioMapper locatarioMapper;

    @Mock
    private AluguelRepository aluguelRepository;

    @Test
    void deveCriarLocatarioComSucesso() {
        // Arrange
        Long locatarioId = 1L;
        LocalDate dataNascimento = LocalDate.of(1990, 5, 15);

        // ENTRADA do serviço: LocatarioRequest
        LocatarioRequest request = new LocatarioRequest(
                "João Silva",
                "12345635748",          // cpf
                "11988887777",          // telefone
                "Masculino",            // sexo
                "joao@email.com",       // email
                dataNascimento
        );

        // Entidade usada internamente
        Locatario locatarioEntidade = new Locatario(); // precisa de @NoArgsConstructor
        locatarioEntidade.setNome(request.nome());
        locatarioEntidade.setCpf(request.cpf());
        locatarioEntidade.setTelefone(request.telefone());
        locatarioEntidade.setSexo(request.sexo());
        locatarioEntidade.setEmail(request.email());
        locatarioEntidade.setDataNascimento(request.dataNascimento());

        // Entidade depois de salvar
        Locatario locatarioSalvo = new Locatario();
        locatarioSalvo.setId(locatarioId);
        locatarioSalvo.setNome(request.nome());
        locatarioSalvo.setCpf(request.cpf());
        locatarioSalvo.setTelefone(request.telefone());
        locatarioSalvo.setSexo(request.sexo());
        locatarioSalvo.setEmail(request.email());
        locatarioSalvo.setDataNascimento(request.dataNascimento());


        LocatarioResponse responseEsperada = new LocatarioResponse(
                locatarioId,
                "João Silva",
                "Masculino",
                "11988887777",
                "joao@email.com",
                dataNascimento,
                "12345635748"
        );

        // Mocks
        when(locatarioMapper.toEntity(ArgumentMatchers.any(LocatarioRequest.class)))
                .thenReturn(locatarioEntidade);

        when(locatarioRepository.save(ArgumentMatchers.<Locatario>any()))
                .thenReturn(locatarioSalvo);

        when(locatarioMapper.toResponse(ArgumentMatchers.any(Locatario.class)))
                .thenReturn(responseEsperada);

        // Act — chame o service com REQUEST
        LocatarioResponse resultado = locatarioService.criarLocatario(request);

        // Assert
        assertNotNull(resultado, "O resultado não deveria ser nulo");
        assertEquals(request.nome(), resultado.nome());
        assertEquals(request.cpf(), resultado.cpf());
        assertEquals(request.sexo(), resultado.sexo());
        assertEquals(request.telefone(), resultado.telefone());
        assertEquals(request.email(), resultado.email());
        assertEquals(request.dataNascimento(), resultado.dataNascimento());

        // Verifica save com any tipado (resolve bound genérico S extends Locatario)
        verify(locatarioRepository, times(1)).save(ArgumentMatchers.<Locatario>any());

        // Captura e checa os campos enviados ao save
        ArgumentCaptor<Locatario> captor = ArgumentCaptor.forClass(Locatario.class);
        verify(locatarioRepository, times(1)).save(captor.capture());
        Locatario salvo = captor.getValue();
        assertEquals("João Silva", salvo.getNome());
        assertEquals("12345635748", salvo.getCpf());
        assertEquals("Masculino", salvo.getSexo());
        assertEquals("11988887777", salvo.getTelefone());
        assertEquals("joao@email.com", salvo.getEmail());
        assertEquals(dataNascimento, salvo.getDataNascimento());
    }



    @Test
    void deveBuscarPorIdComSucesso() throws ChangeSetPersister.NotFoundException {
        Long id = 10L;
        LocalDate dn = LocalDate.of(1990, 1, 1);

        Locatario entity = new Locatario();
        entity.setId(id);
        entity.setNome("João");
        entity.setCpf("12345678901");
        entity.setTelefone("11999990000");
        entity.setSexo("Masculino");
        entity.setEmail("joao@email.com");
        entity.setDataNascimento(dn);

        LocatarioResponse response = new LocatarioResponse(
                id, "João", "Masculino", "11999990000", "joao@email.com", dn, "12345678901"
        );

        when(locatarioRepository.findById(id)).thenReturn(Optional.of(entity));
        when(locatarioMapper.toResponse(entity)).thenReturn(response);

        LocatarioResponse resultado = locatarioService.buscarLocatarioPorId(id);

        assertNotNull(resultado);
        assertEquals("João", resultado.nome());
        assertEquals("12345678901", resultado.cpf());
        verify(locatarioRepository, times(1)).findById(id);
        verify(locatarioMapper, times(1)).toResponse(entity);
    }


    @Test
    void deveLancarNotFound_quandoBuscarPorIdInexistente() {
        Long id = 999L;
        when(locatarioRepository.findById(id)).thenReturn(Optional.empty());

        ChangeSetPersister.NotFoundException ex = assertThrows(ChangeSetPersister.NotFoundException.class,
                () -> locatarioService.buscarLocatarioPorId(id));

        assertTrue(ex.getMessage().contains("id=" + id));
        verify(locatarioRepository, times(1)).findById(id);
        verifyNoInteractions(locatarioMapper);
    }


    @Test
    void deveListarTodosLocatarios() {
        LocalDate dn1 = LocalDate.of(1990, 5, 15);
        LocalDate dn2 = LocalDate.of(1985, 8, 20);

        Locatario e1 = new Locatario();
        e1.setId(1L);
        e1.setNome("João");
        e1.setSexo("Masculino");
        e1.setTelefone("11988887777");
        e1.setEmail("joao@email.com");
        e1.setDataNascimento(dn1);
        e1.setCpf("12345678901");

        Locatario e2 = new Locatario();
        e2.setId(2L);
        e2.setNome("Maria");
        e2.setSexo("Feminino");
        e2.setTelefone("11977776666");
        e2.setEmail("maria@email.com");
        e2.setDataNascimento(dn2);
        e2.setCpf("98765432100");

        when(locatarioRepository.findAll()).thenReturn(List.of(e1, e2));

        LocatarioResponse r1 = new LocatarioResponse(1L, "João", "Masculino", "11988887777", "joao@email.com", dn1, "12345678901");
        LocatarioResponse r2 = new LocatarioResponse(2L, "Maria", "Feminino", "11977776666", "maria@email.com", dn2, "98765432100");

        when(locatarioMapper.toResponse(e1)).thenReturn(r1);
        when(locatarioMapper.toResponse(e2)).thenReturn(r2);

        List<LocatarioResponse> lista = locatarioService.listarTodosLocatarios();

        assertNotNull(lista);
        assertEquals(2, lista.size());
        assertEquals("João", lista.get(0).nome());
        assertEquals("Maria", lista.get(1).nome());

        verify(locatarioRepository, times(1)).findAll();
        verify(locatarioMapper, times(1)).toResponse(e1);
        verify(locatarioMapper, times(1)).toResponse(e2);
    }

    @Test
    void deveRetornarListaVazia_quandoNaoExistiremLocatarios() {
        when(locatarioRepository.findAll()).thenReturn(List.of());

        List<LocatarioResponse> lista = locatarioService.listarTodosLocatarios();

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
        verify(locatarioRepository, times(1)).findAll();
        verifyNoInteractions(locatarioMapper); // não mapeia nada se não há entidades
    }





    @Test
    void deveDeletarLocatarioComSucesso() {
        // Arrange
        Long id = 7L;

        Locatario existente = new Locatario(); // exige @NoArgsConstructor no entity
        existente.setId(id);

        when(locatarioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(aluguelRepository.existsByLocatarioId(id)).thenReturn(false);


        // Act + Assert
        assertDoesNotThrow(() -> locatarioService.deletarLocatario(id));

        // Verificações
        verify(locatarioRepository, times(1)).findById(id);
        verify(aluguelRepository, times(1)).existsByLocatarioId(id);
        verify(locatarioRepository, times(1)).delete(existente);
        verify(locatarioRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(locatarioRepository, aluguelRepository);
    }



    @Test
    void deveLancarNotFound_quandoDeletarIdInexistente() {
        Long id = 999L;

        when(locatarioRepository.existsById(id)).thenReturn(false);

        ChangeSetPersister.NotFoundException ex = assertThrows(ChangeSetPersister.NotFoundException.class,
                () -> locatarioService.deletarLocatario(id));

        assertTrue(ex.getMessage().contains("id=" + id));
        verify(locatarioRepository, times(1)).existsById(id);
        verify(locatarioRepository, never()).deleteById(anyLong());
    }



}





