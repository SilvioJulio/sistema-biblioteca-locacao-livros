package br.com.academia.bibliotecalocacao.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record AluguelDTO(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataRetirada,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataDevolucao,
        Long livroId,
        String livroNome,
        Long locatarioId,
        String locatarioNome
) {
}
