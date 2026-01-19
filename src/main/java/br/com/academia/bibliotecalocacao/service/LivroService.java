package br.com.academia.bibliotecalocacao.service;


import br.com.academia.bibliotecalocacao.dtos.response.LivroResponse;
import br.com.academia.bibliotecalocacao.mapper.LivroMapper;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public LivroResponse criarLivro (LivroResponse livroResponse){
        var livroEntity = LivroMapper .toEntity(livroResponse);
        var livroSalvo = livroRepository.save(livroEntity);
        return LivroMapper.toResponse(livroSalvo);

    }

    public LivroResponse buscarLivroPorId (Long livroId){
        return livroRepository.findById(livroId)
                .map(LivroMapper ::toResponse)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + livroId));
    }

    public LivroResponse autalizarLivro (Long livroId, LivroResponse livroResponse){
        var livroExistente = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + livroId));

        livroExistente.setNome(livroResponse.nome());
        livroExistente.setIsbn(livroResponse.isbn());
        livroExistente.setDataPublicacao(livroResponse.dataPublicacao());

        var livroAtualizado = livroRepository.save(livroExistente);
        return LivroMapper.toResponse(livroAtualizado);
    }



}
