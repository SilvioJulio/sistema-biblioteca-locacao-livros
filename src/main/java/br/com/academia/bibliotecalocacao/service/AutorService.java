package br.com.academia.bibliotecalocacao.service;

import br.com.academia.bibliotecalocacao.dtos.response.AutorResponse;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;


    public AutorResponse buscarAutorPorLivroId(Long livroId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + livroId));

        var autor = livro.getAutor();

        return new AutorResponse(
                autor.getId(),
                autor.getNome(),
                autor.getSexo(),
                autor.getAnoNascimento(),
                autor.getCpf()
        );
    }






}
