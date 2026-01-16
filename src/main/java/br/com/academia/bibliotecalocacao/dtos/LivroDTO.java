package br.com.academia.bibliotecalocacao.dtos;

public record LivroDTO(
        Long id,
        String nome,
        String isbn,
        Integer anoPublicacao,
        AutorDTO autor
) {
}
