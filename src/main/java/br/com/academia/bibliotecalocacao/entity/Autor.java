package br.com.academia.bibliotecalocacao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "autores")
@Setter
@Getter
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do autor é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    private String sexo;

    @NotNull(message = "A nacionalidade do autor é obrigatória")
    @Column(name = "ano_nascimento", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Integer anoNascimento;

    @NotBlank(message = "O CPF do autor é obrigatório")
    @Column(nullable = false, unique = true, length = 11)
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
    private String cpf;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Livro> livros;

}
