package br.com.academia.bibliotecalocacao.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record AutorRequest(

            @NotBlank (message = "O nome do autor é obrigatório")
            String  nome,
            String sexo,
            Integer anoNascimento,
            String cpf,
            Long livroId
)
{ }
