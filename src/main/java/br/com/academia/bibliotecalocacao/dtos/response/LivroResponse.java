package br.com.academia.bibliotecalocacao.dtos.response;

import java.time.LocalDate;

public record LivroResponse(
        Long id,
        String nome,
        String isbn,
        LocalDate dataPublicacao,
        AutorResponse autor
) {
}
