package br.com.academia.bibliotecalocacao.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;


public record LocatarioResponse(
        Long id,
        String nome,
        String sexo,
        String telefone,
        String email,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataNascimento,
        String cpf

) { }

