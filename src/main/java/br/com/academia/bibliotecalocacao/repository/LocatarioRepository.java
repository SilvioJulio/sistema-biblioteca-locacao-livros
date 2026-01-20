
package br.com.academia.bibliotecalocacao.repository;

import br.com.academia.bibliotecalocacao.entity.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocatarioRepository extends JpaRepository<Locatario, Long> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Optional<Locatario> findByCpf(String cpf);
}
