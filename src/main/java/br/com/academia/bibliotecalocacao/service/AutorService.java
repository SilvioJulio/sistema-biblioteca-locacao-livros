package br.com.academia.bibliotecalocacao.service;

import br.com.academia.bibliotecalocacao.dtos.response.AutorResponse;
import br.com.academia.bibliotecalocacao.mapper.AutorMapper;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    private final LivroRepository livroRepository;

    private final  AutorMapper autorMapper;


    public AutorResponse criarAutor(AutorResponse autorResponse) {
        var autorEntity = autorMapper.toEntity(autorResponse);
        var autorSalvo = autorRepository.save(autorEntity);
        return autorMapper.toResponse(autorSalvo);
    }

    public AutorResponse buscarAutorPorId(Long id) {
        return autorRepository.findById(id)
                .map(autorMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com ID: " + id));
    }

    public Page<AutorResponse> listarTodosAutores(Pageable pageable) {
        return autorRepository.findAll(pageable)
                .map(autorMapper::toResponse);
    }

    public AutorResponse atualizarAutor(Long autorId, AutorResponse autorResponse) {
        var autorExistente = autorRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com ID: " + autorId));

        autorExistente.setNome(autorResponse.nome());
        autorExistente.setSexo(autorResponse.sexo());
        autorExistente.setAnoNascimento(autorResponse.anoNascimento());
        autorExistente.setCpf(autorResponse.cpf());

        var autorAtualizado = autorRepository.save(autorExistente);
        return autorMapper.toResponse(autorAtualizado);
    }

    public void deletarAutor(Long autorId) {
        var autorExistente = autorRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com ID: " + autorId));

        boolean hasLivros = livroRepository.existsByAutorId(autorId);
        if (hasLivros) {
            throw new RuntimeException("Não é possível deletar o autor, existem livros associados a ele.");
        }

        autorRepository.delete(autorExistente);
    }

}
