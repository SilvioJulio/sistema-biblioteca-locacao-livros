package br.com.academia.bibliotecalocacao.mapper;

import br.com.academia.bibliotecalocacao.dtos.AluguelDTO;

public class AlugueMapper {
    public static AluguelDTO toDTO(br.com.academia.bibliotecalocacao.entity.Aluguel aluguel) {
        if (aluguel == null) {
            return null;
        }
        return new AluguelDTO(
                aluguel.getId(),
                aluguel.getDataRetirada(),
                aluguel.getDataDevolucao(),

                aluguel.getLivro() != null ? aluguel.getLivro().getId() : null,
                aluguel.getLivro() != null ? aluguel.getLivro().getNome() : null,

                aluguel.getLocatario() != null ? aluguel.getLocatario().getId() : null,
                aluguel.getLocatario() != null ? aluguel.getLocatario().getNome() : null
        );
    }


}
