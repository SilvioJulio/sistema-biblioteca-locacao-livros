package br.com.academia.bibliotecalocacao.mapper;


import br.com.academia.bibliotecalocacao.dtos.request.LivroRequest;
import br.com.academia.bibliotecalocacao.dtos.response.AutorResponse;
import br.com.academia.bibliotecalocacao.dtos.response.LivroResponse;
import br.com.academia.bibliotecalocacao.entity.Livro;


import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class LivroMapper {

    private final AutorMapper autorMapper;


    public Livro toEntity(LivroRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("LivroRequest não pode ser nulo");
        }

        Livro livro = new Livro();
        livro.setNome(request.nome());
        livro.setIsbn(request.isbn());
        livro.setDataPublicacao(request.dataPublicacao());

        return livro;
    }


    public LivroResponse toResponse(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }

        AutorResponse autorResp = null;
        if (livro.getAutor() != null) {
            autorResp = autorMapper.toResponse(livro.getAutor());
        }

        return new LivroResponse(
                livro.getId(),
                livro.getNome(),
                livro.getIsbn(),
                livro.getDataPublicacao(),
                autorResp
        );
    }

}




