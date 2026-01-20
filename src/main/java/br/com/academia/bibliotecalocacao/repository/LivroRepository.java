
package br.com.academia.bibliotecalocacao.repository;

import br.com.academia.bibliotecalocacao.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("""
           SELECT l FROM Livro l
           WHERE NOT EXISTS (
               SELECT 1 FROM Aluguel a
               WHERE a.livro = l
               AND (a.dataDevolucao IS NULL OR a.dataDevolucao >= CURRENT_DATE)
           )
           """)
    List<Livro> findLivrosDisponiveis();

    List<Livro> findByAutorNomeContainingIgnoreCase(String nome);

    List<Livro> findByAutorId(Long autorId);

    boolean existsByAutorId(Long autorId);

    boolean existsByIsbn(String isbn);
}

