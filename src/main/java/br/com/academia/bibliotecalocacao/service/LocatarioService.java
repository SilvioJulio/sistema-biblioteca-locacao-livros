package br.com.academia.bibliotecalocacao.service;

import br.com.academia.bibliotecalocacao.dtos.request.LocatarioRequest;
import br.com.academia.bibliotecalocacao.dtos.response.LocatarioResponse;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.exception.LocatarioNotFoundException;
import br.com.academia.bibliotecalocacao.mapper.LocatarioMapper;
import br.com.academia.bibliotecalocacao.repository.AluguelRepository;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.transaction.annotation.Transactional;
import br.com.academia.bibliotecalocacao.exception.LocatarioNotFoundException;



import java.util.List;

@Service
@RequiredArgsConstructor
public class LocatarioService {


    private final LocatarioRepository locatarioRepository;
    private final LocatarioMapper locatarioMapper;
    private final AluguelRepository aluguelRepository;

    public LocatarioResponse criarLocatario(@Valid LocatarioRequest request) {
        var entity = locatarioMapper.toEntity(request);
        var salvo = locatarioRepository.save(entity);
        return locatarioMapper.toResponse(salvo);
    }

    // >>> declare throws <<<
    public LocatarioResponse buscarLocatarioPorId(Long locatarioId)
            throws ChangeSetPersister.NotFoundException {
        var locatario = locatarioRepository.findById(locatarioId)
                .orElseThrow(() -> new LocatarioNotFoundException(locatarioId));
        return locatarioMapper.toResponse(locatario);
    }

    // >>> declare throws <<<
    public LocatarioResponse atualizarLocatario(Long locatarioId, @Valid LocatarioRequest request)
            throws ChangeSetPersister.NotFoundException {
        var existente = locatarioRepository.findById(locatarioId)
                .orElseThrow(() -> new LocatarioNotFoundException(locatarioId));

        existente.setNome(request.nome());
        existente.setCpf(request.cpf());
        existente.setTelefone(request.telefone());
        existente.setEmail(request.email());
        existente.setDataNascimento(request.dataNascimento());

        var atualizado = locatarioRepository.save(existente);
        return locatarioMapper.toResponse(atualizado);
    }

    public List<LocatarioResponse> listarTodosLocatarios() {
        return locatarioRepository.findAll()
                .stream()
                .map(locatarioMapper::toResponse)
                .toList();
    }


    @Transactional
    public void deletarLocatario(Long locatarioId)
            throws ChangeSetPersister.NotFoundException {


        Locatario existente = locatarioRepository.findById(locatarioId)
                .orElseThrow(() -> new LocatarioNotFoundException(locatarioId));


        boolean hasAlugueis = aluguelRepository.existsByLocatarioId(locatarioId);
        if (hasAlugueis) {
            throw new RuntimeException("Não é possível deletar o locatário, existem alugueis associados a ele.");
        }

        locatarioRepository.delete(existente);
    }


}
