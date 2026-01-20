package br.com.academia.bibliotecalocacao.dtos.request;

public record LocatarioRequest(
        String nome,
        String telefone,
        String email

) {
}
