package br.com.academia.bibliotecalocacao.mapper;

import br.com.academia.bibliotecalocacao.dtos.response.AluguelResponseDTO;

public class AlugueMapper {
    public static AluguelResponseDTO toDTO(br.com.academia.bibliotecalocacao.entity.Aluguel aluguel) {
        if (aluguel == null) {
            return null;
        }
        return new AluguelResponseDTO(
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
