package br.com.academia.bibliotecalocacao.dtos.response;


public record AutorResponse(

        Long id,
        String nome,
        String sexo,
        Integer anoNascimento,
        String cpf

) {

}
