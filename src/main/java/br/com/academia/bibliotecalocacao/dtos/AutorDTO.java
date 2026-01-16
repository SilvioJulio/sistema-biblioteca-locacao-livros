package br.com.academia.bibliotecalocacao.dtos;

import java.time.LocalDate;

public record AutorDTO(

        Long id,
        String nome,
        String sexo,
        LocalDate anoNascimento,
        String cpf

) {

}
