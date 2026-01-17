package br.com.academia.bibliotecalocacao.dtos.response;

public record LivroResponse(
        Long id,
        String nome,
        String isbn,
        Integer anoPublicacao,
        AutorResponse autor
) {
}
