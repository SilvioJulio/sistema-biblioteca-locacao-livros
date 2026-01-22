package br.com.academia.bibliotecalocacao.dtos.request;

public record AluguelRequest(
        Long id,
        Long locatarioId,
        int i) { }
