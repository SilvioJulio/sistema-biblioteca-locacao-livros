package br.com.academia.bibliotecalocacao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "livros")
@Setter
@Getter
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do livro é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "O ISBN do livro é obrigatório")
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotNull(message = "A data de publicação do livro é obrigatória")
    @Column(name = "data_publicacao", nullable = false)
    private LocalDate dataPublicacao;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    @JsonIgnoreProperties("livros") // Evita que, ao carregar o autor, ele tente carregar os livros dele de novo
    private Autor autor;
}


