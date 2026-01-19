package br.com.academia.bibliotecalocacao.repository;

import br.com.academia.bibliotecalocacao.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("SELECT l FROM Livro l WHERE l.id NOT IN (SELECT a.livro.id FROM Aluguel a WHERE a.dataDevolucao >= CURRENT_DATE)")
    List<Livro> findLivrosDisponiveis();

    List<Livro> findByAutorNomeContainingIgnoreCase(String nome);

    List<Livro> findByAutorId(Long autorId);

    boolean existsByIsbn(String isbn);

    boolean existsByAutorId (Long autorId);
}
