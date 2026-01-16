package br.com.academia.bibliotecalocacao.dtos;

public record LocatarioDTO(
        Long id,
        String nome,
        String cpf,
        String telefone
) {
}
