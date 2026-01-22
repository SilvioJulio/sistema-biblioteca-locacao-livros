package br.com.academia.bibliotecalocacao.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;


import io.swagger.v3.oas.annotations.media.Schema;

public record LocatarioRequest(
        @Schema(description = "Nome completo do locatário", example = "João da Silva")
        String nome,

        @Schema(description = "CPF formatado ou apenas números", example = "123.456.789-00")
        String cpf,

        @Schema(description = "Telefone com DDD", example = "11988887777")
        String telefone,

        @Schema(description = "Gênero do locatário", example = "Masculino")
        String sexo,

        @Schema(description = "E-mail de contato", example = "joao.silva@email.com")
        String email,

        @Schema(
                description = "Data de nascimento no formato brasileiro",
                example = "25/12/1990",
                type = "string",
                pattern = "dd/MM/yyyy"
        )
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento
) { }


