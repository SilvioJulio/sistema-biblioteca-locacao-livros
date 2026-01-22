
package br.com.academia.bibliotecalocacao.repository;

import br.com.academia.bibliotecalocacao.dtos.response.AluguelResponse;
import br.com.academia.bibliotecalocacao.entity.Aluguel;
import br.com.academia.bibliotecalocacao.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    @Override
    @EntityGraph(attributePaths = {"livro", "locatario"})
    Page<Aluguel> findAll(Pageable pageable);

    @Query("SELECT a.livro FROM Aluguel a WHERE a.dataDevolucao >= CURRENT_DATE")
    List<Livro> findLivrosAlugados();

    boolean existsByLivroId(Long livroId);

    @Query("SELECT a.livro FROM Aluguel a WHERE a.locatario.id = :locatarioId AND a.dataDevolucao >= CURRENT_DATE")
    List<Livro> findLivrosAlugadosPorLocatarioId(@Param("locatarioId") Long locatarioId);

    boolean existsByLocatarioId(Long locatarioId);
}


