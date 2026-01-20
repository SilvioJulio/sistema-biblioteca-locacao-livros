
package br.com.academia.bibliotecalocacao.repository;

import br.com.academia.bibliotecalocacao.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    List<Autor> findByNome(String nome);
    List<Autor> findByNomeContainingIgnoreCase(String nome);
}
