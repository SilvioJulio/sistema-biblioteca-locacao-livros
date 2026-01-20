package br.com.academia.bibliotecalocacao.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record LivroRequest(
        @NotBlank(message = "O nome do autor é obrigatório")
        String nome,

        @NotBlank(message = "O ISBN é obrigatório")
        // Validação via Regex para aceitar ISBN de 10 ou 13 dígitos
        @Pattern(regexp = "^(?:97[89])?\\d{9}[\\dxX]$", message = "ISBN inválido")
        String isbn,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataPublicacao,
        Long autorId
) {
}
