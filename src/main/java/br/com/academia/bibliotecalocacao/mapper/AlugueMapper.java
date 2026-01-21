package br.com.academia.bibliotecalocacao.mapper;

import br.com.academia.bibliotecalocacao.dtos.response.AluguelResponse;
import br.com.academia.bibliotecalocacao.entity.Aluguel;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import org.springframework.stereotype.Component;


public class AlugueMapper {
    public static AluguelResponse toDTO(br.com.academia.bibliotecalocacao.entity.Aluguel aluguel) {
        if (aluguel == null) {
            throw new IllegalArgumentException("Aluguel não pode ser nulo");
        }
        return new AluguelResponse(
                aluguel.getId(),
                aluguel.getDataRetirada(),
                aluguel.getDataDevolucao(),

                aluguel.getLivro() != null ? aluguel.getLivro().getId() : null,
                aluguel.getLivro() != null ? aluguel.getLivro().getNome() : null,

                aluguel.getLocatario() != null ? aluguel.getLocatario().getId() : null,
                aluguel.getLocatario() != null ? aluguel.getLocatario().getNome() : null
        );
    }

    public static Aluguel toEntity(Livro livro, Locatario locatario) {
        Aluguel aluguel = new Aluguel();
        aluguel.setLivro(livro);
        aluguel.setLocatario(locatario);
        aluguel.setDataRetirada(java.time.LocalDate.now());
        aluguel.setDataDevolucao(java.time.LocalDate.now().plusDays(2));
        return aluguel;
    }


}
