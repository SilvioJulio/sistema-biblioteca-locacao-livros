package br.com.academia.bibliotecalocacao.service;

import br.com.academia.bibliotecalocacao.dtos.request.LocatarioRequest;
import br.com.academia.bibliotecalocacao.dtos.response.LocatarioResponse;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.mapper.LocatarioMapper;
import br.com.academia.bibliotecalocacao.repository.AluguelRepository;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocatarioService {

    private final LocatarioRepository locatarioRepository;
    private final LocatarioMapper locatarioMapper; // Injeção da instância
    private final AluguelRepository aluguelRepository;



    public LocatarioResponse criarLocatario(@Valid LocatarioRequest locatarioResponse) {
        var locatarioEntity = locatarioMapper.toEntity(locatarioResponse);
        var locatarioSalvo = locatarioRepository.save(locatarioEntity);
        return locatarioMapper.toResponse(locatarioSalvo);
    }


    public LocatarioResponse buscarLocatarioPorId(Long locatarioId) {
        return locatarioRepository.findById(locatarioId)
                // CORREÇÃO: use a referência da instância 'locatarioMapper::toResponse'
                .map(locatarioMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado com ID: " + locatarioId));
    }


    public LocatarioResponse atualizarLocatario(Long locatarioId, LocatarioResponse locatarioResponse) {
        var locatarioExistente = locatarioRepository.findById(locatarioId)
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado com ID: " + locatarioId));

        // Atualizando os campos da entidade com os dados do Record (Java 21 syntax)
        locatarioExistente.setNome(locatarioResponse.nome());
        locatarioExistente.setCpf(locatarioResponse.cpf());
        locatarioExistente.setTelefone(locatarioResponse.telefone());
        locatarioExistente.setEmail(locatarioResponse.email());
        locatarioExistente.setDataNascimento(locatarioResponse.dataNascimento());

        var locatarioAtualizado = locatarioRepository.save(locatarioExistente);

        // CORREÇÃO: Chamada via instância injetada
        return locatarioMapper.toResponse(locatarioAtualizado);
    }

    public List<LocatarioResponse> listarTodosLocatarios() {
        List<Locatario> locatarios = locatarioRepository.findAll();
        return locatarios.stream()

                .map(locatarioMapper::toResponse)
                .toList();
    }

    public void deletarLocatario(Long locatarioId) {
        var locatarioExistente = locatarioRepository.findById(locatarioId)
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado com ID: " + locatarioId));

        // Agora o 'aluguelRepository' será reconhecido
        boolean hasAlugueis = aluguelRepository.existsByLocatarioId(locatarioId);

        if (hasAlugueis) {
            throw new RuntimeException("Não é possível deletar o locatário, existem alugueis associados a ele.");
        }

        locatarioRepository.delete(locatarioExistente);
    }


}
