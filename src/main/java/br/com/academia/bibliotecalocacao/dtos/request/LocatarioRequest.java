package br.com.academia.bibliotecalocacao.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;


public record LocatarioRequest(
        String nome,
        String cpf,
        String telefone,
        String sexo,
        String email,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataNascimento
) { }

