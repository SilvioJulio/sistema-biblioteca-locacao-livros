package br.com.academia.bibliotecalocacao.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record AluguelResponseDTO(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataRetirada,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataDevolucao,
        Long livroId,
        String livroNome,
        Long locatarioId,
        String locatarioNome
) {
}
