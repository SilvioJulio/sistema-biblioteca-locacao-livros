package br.com.academia.bibliotecalocacao.controller;


import br.com.academia.bibliotecalocacao.dtos.request.AluguelRequest;
import br.com.academia.bibliotecalocacao.dtos.response.AluguelResponse;
import br.com.academia.bibliotecalocacao.service.AluguelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/alugueis")
@RequiredArgsConstructor
public class AluguelController {

    private final AluguelService aluguelService;

    @PostMapping
    public ResponseEntity<AluguelResponse> criar(@Valid @RequestBody AluguelRequest request) {
        var response = aluguelService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AluguelResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(aluguelService.buscarPorId(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<AluguelResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AluguelRequest request) {
        var resp = aluguelService.atualizar(id, request);
        return ResponseEntity.ok(resp);
    }


    @GetMapping
    public ResponseEntity<Page<AluguelResponse>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(aluguelService.listarTodos(pageable));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<AluguelResponse> deletar(@PathVariable Long id) {
        aluguelService.deletar(id);
        return ResponseEntity.noContent().build();

    }


}

