package br.com.academia.bibliotecalocacao.service;


import br.com.academia.bibliotecalocacao.dtos.request.LivroRequest;
import br.com.academia.bibliotecalocacao.dtos.response.LivroResponse;
import br.com.academia.bibliotecalocacao.entity.Autor;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.mapper.LivroMapper;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final LivroMapper livroMapper;

    public LivroResponse criarLivro(LivroRequest request) {

        if (livroRepository.existsByIsbn(request.isbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado");
        }

        Livro livro = livroMapper.toEntity(request);

        if (request.autorId() != null) {
            Autor autor = autorRepository.findById(request.autorId())
                    .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
            livro.setAutor(autor);
        }

        Livro salvo = livroRepository.save(livro);
        return livroMapper.toResponse(salvo);
    }

    public LivroResponse buscarLivroPorId(Long livroId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        return livroMapper.toResponse(livro);
    }

    public LivroResponse atualizarLivro(Long livroId, LivroRequest request) {

        Livro existente = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (!existente.getIsbn().equals(request.isbn())
                && livroRepository.existsByIsbn(request.isbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado");
        }

        existente.setNome(request.nome());
        existente.setIsbn(request.isbn());
        existente.setDataPublicacao(request.dataPublicacao());

        if (request.autorId() != null) {
            Autor autor = autorRepository.findById(request.autorId())
                    .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
            existente.setAutor(autor);
        }

        Livro atualizado = livroRepository.save(existente);
        return livroMapper.toResponse(atualizado);
    }

    public List<LivroResponse> listarTodosLivros() {
        return livroRepository.findAll().stream()
                .map(livroMapper::toResponse)
                .toList();
    }

    public void  deletarLivroPorId(Long livroId) {
        if (!livroRepository.existsById(livroId)) {
            throw new RuntimeException("Livro não encontrado");
        }
        livroRepository.deleteById(livroId);
    }
}





