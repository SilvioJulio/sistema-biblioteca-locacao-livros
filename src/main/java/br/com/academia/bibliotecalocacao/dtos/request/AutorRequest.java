package br.com.academia.bibliotecalocacao.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

public record AutorRequest(

            @NotBlank (message = "O nome do autor é obrigatório")
            String  nome,
            String sexo,

            @NotNull (message = "O ano de nascimento é obrigatório")
            @Past (message = "O ano de nascimento deve ser uma data passada")
            Integer anoNascimento,

            @NotNull (message = "O CPF do autor é obrigatório")
            @CPF (message = "O CPF informado é inválido")
            String cpf,

            @NotNull (message = "O ID do livro é obrigatório")
            Long livroId
)
{ }
