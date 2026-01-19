package br.com.academia.bibliotecalocacao.mapper;


import br.com.academia.bibliotecalocacao.dtos.response.LivroResponse;
import br.com.academia.bibliotecalocacao.entity.Livro;

public class LivroMapper {

    public static LivroResponse  toResponse(Livro livro) {
        if (livro == null){
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }

        return new LivroResponse(
                livro.getId(),
                livro.getNome(),
                livro.getIsbn(),
                livro.getDataPublicacao(),
                AutorMapper.toResponse(livro.getAutor()
        ));


    }

    public static Livro toEntity(LivroResponse livroResponse) {
        if (livroResponse == null) {
            throw new IllegalArgumentException("LivroResponse não pode ser nulo");
        }

        Livro livro = new Livro();
        livro.setId(livroResponse.id());
        livro.setNome(livroResponse.nome());
        livro.setIsbn(livroResponse.isbn());
        livro.setDataPublicacao(livroResponse.dataPublicacao());
        livro.setAutor(AutorMapper.toEntity(livroResponse.autor()));

        return livro;
    }


}
