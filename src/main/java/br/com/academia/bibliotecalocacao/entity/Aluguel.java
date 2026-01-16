package br.com.academia.bibliotecalocacao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "alugueis")
@Setter
@Getter
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataRetirada = LocalDate.now();

    private LocalDate dataDevolucao = dataRetirada.plusDays(2);

    @ManyToOne
    @JoinColumn(name = "locatario_id", nullable = false)
    private Locatario locatario;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;
}
