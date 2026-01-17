package br.com.academia.bibliotecalocacao.dtos.response;

public record LocatarioResponse(
        Long id,
        String nome,
        String cpf,
        String telefone
) {
}
