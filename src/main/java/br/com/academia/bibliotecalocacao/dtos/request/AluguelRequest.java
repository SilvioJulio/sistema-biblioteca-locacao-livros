package br.com.academia.bibliotecalocacao.dtos.request;

public record AluguelRequest(
        Long livroId,
        Long locatarioId
) { }
