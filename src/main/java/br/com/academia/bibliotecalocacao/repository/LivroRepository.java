package br.com.academia.bibliotecalocacao.repository;

import br.com.academia.bibliotecalocacao.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByAutorNOmeContainingIngnoreCase(String nome);

    List<Livro> findByIdAutorId(Long autorId);
}
