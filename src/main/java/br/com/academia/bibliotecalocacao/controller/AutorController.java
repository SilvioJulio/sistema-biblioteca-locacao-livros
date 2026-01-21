package br.com.academia.bibliotecalocacao.controller;


import br.com.academia.bibliotecalocacao.dtos.response.AutorResponse;
import br.com.academia.bibliotecalocacao.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autores")
@RequiredArgsConstructor
public class AutorController {


    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<AutorResponse> criar(@RequestBody AutorResponse request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.criarAutor(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.buscarAutorPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<AutorResponse>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(autorService.listarTodosAutores(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponse> atualizar(@PathVariable Long id, @RequestBody AutorResponse request) {
        return ResponseEntity.ok(autorService.atualizarAutor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        autorService.deletarAutor(id);
        return ResponseEntity.noContent().build();
    }

}
