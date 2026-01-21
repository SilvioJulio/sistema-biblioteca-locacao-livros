package br.com.academia.bibliotecalocacao.service;


import br.com.academia.bibliotecalocacao.dtos.request.LivroRequest;
import br.com.academia.bibliotecalocacao.dtos.response.LivroResponse;
import br.com.academia.bibliotecalocacao.entity.Autor;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.mapper.LivroMapper;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final LivroMapper livroMapper;

    public LivroResponse criarLivro(@Valid LivroRequest request) {
        if (livroRepository.existsByIsbn(request.isbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado");
        }
        if (request.autorId() == null) {
            throw new IllegalArgumentException("autorId é obrigatório");
        }

        // 1) Mapear antes (para satisfazer o teste)
        Livro livro = livroMapper.toEntity(request);

        // 2) Buscar autor (pode lançar)
        Autor autor = autorRepository.findById(request.autorId())
                .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado para id=" + request.autorId()));

        // 3) Setar autor e salvar
        livro.setAutor(autor);
        Livro salvo = livroRepository.save(livro);
        return livroMapper.toResponse(salvo);
    }

    public LivroResponse buscarLivroPorId(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        return livroMapper.toResponse(livro);
    }

    public LivroResponse atualizarLivro(Long id, @Valid LivroRequest request) {
        Livro existente = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (!existente.getIsbn().equals(request.isbn()) && livroRepository.existsByIsbn(request.isbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado");
        }

        Autor autor = autorRepository.findById(request.autorId())
                .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado para id=" + request.autorId()));

        existente.setNome(request.nome());
        existente.setIsbn(request.isbn());
        existente.setDataPublicacao(request.dataPublicacao());
        existente.setAutor(autor);

        Livro salvo = livroRepository.save(existente);
        return livroMapper.toResponse(salvo);
    }

    public List<LivroResponse> listarTodosLivros() {
        return livroRepository.findAll()
                .stream()
                .map(livroMapper::toResponse)
                .toList();
    }

    public void deletarLivroPorId(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new IllegalArgumentException("Livro não encontrado para id=" + id);
        }
        livroRepository.deleteById(id);
    }
}









