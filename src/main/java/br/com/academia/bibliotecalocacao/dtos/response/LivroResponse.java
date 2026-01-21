package br.com.academia.bibliotecalocacao.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record LivroResponse(

        Long id,
        String nome,
        String isbn,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataPublicacao,
        AutorResponse autor


) {
}
