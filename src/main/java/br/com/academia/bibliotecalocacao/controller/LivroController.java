package br.com.academia.bibliotecalocacao.controller;


import br.com.academia.bibliotecalocacao.dtos.request.LivroRequest;
import br.com.academia.bibliotecalocacao.dtos.response.LivroResponse;
import br.com.academia.bibliotecalocacao.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor

public class LivroController {

    private final LivroService livroService; // precisa ser final para Lombok gerar o construtor

    @PostMapping
    public ResponseEntity<LivroResponse> criar(@Valid @RequestBody LivroRequest request) {
        LivroResponse criado = livroService.criarLivro(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarLivroPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponse> atualizar(@PathVariable Long id,
                                                   @Valid @RequestBody LivroRequest request) {
        return ResponseEntity.ok(livroService.atualizarLivro(id, request));
    }

    @GetMapping
    public ResponseEntity<List<LivroResponse>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodosLivros());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        livroService.deletarLivroPorId(id);
        return ResponseEntity.noContent().build();
    }

}
