package br.com.academia.bibliotecalocacao.repository;

import br.com.academia.bibliotecalocacao.entity.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocatarioRepository extends JpaRepository<Locatario, Long> {
    // Para validar a unicidade do CPF ou E-mail antes de cadastra
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);


    // Caso precise buscar um locatário específico por CPF
    Optional<Locatario> findByCpf(String cpf);


}
