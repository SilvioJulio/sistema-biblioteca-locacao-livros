package br.com.academia.bibliotecalocacao.mapper;

import br.com.academia.bibliotecalocacao.dtos.response.AutorResponse;
import br.com.academia.bibliotecalocacao.entity.Autor;

public class AutorMapper {

    // Converte de Entidade para DTO de Resposta (GET / Saída)
    public static AutorResponse toResponse(Autor autor) {
        if (autor == null){
            throw new IllegalArgumentException("Autor não pode ser nulo");
        }

        return new AutorResponse(
                autor.getId(),
                autor.getNome(),
                autor.getSexo(),
                autor.getAnoNascimento(),
                autor.getCpf()

        );

    }
    // Converte de DTO de Requisição para Entidade (POST/PUT / Entrada)
    public static Autor toEntity(AutorResponse autorResponse) {
        if (autorResponse == null) {
            throw new IllegalArgumentException("AutorResponse não pode ser nulo");
        }

        Autor autor = new Autor();
        autor.setId(autorResponse.id());
        autor.setNome(autorResponse.nome());
        autor.setSexo(autorResponse.sexo());
        // Se a Entity espera Integer (ano) e o Request é LocalDate, extraímos o ano:
        autor.setAnoNascimento(autorResponse.anoNascimento());
        autor.setCpf(autorResponse.cpf());

        return autor;
    }
}
