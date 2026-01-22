package br.com.academia.bibliotecalocacao.controller;

import br.com.academia.bibliotecalocacao.dtos.request.LocatarioRequest;
import br.com.academia.bibliotecalocacao.dtos.response.LocatarioResponse;
import br.com.academia.bibliotecalocacao.service.LocatarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locatarios")
@RequiredArgsConstructor
public class LocatarioController {

    private final LocatarioService locatarioService;

    @PostMapping
    public ResponseEntity<LocatarioResponse> criar(@Valid @RequestBody LocatarioRequest request) {
        var resposta = locatarioService.criarLocatario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocatarioResponse> locatarioPorId(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        // Removido o 'throws', o Service já cuida da exceção se não encontrar
        var resp = locatarioService.buscarLocatarioPorId(id);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocatarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LocatarioRequest request) throws ChangeSetPersister.NotFoundException {
        // Ajustado para receber LocatarioRequest no corpo
        var resp = locatarioService.atualizarLocatario(id, request);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<LocatarioResponse>> listarTodos() {
        return ResponseEntity.ok(locatarioService.listarTodosLocatarios());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        locatarioService.deletarLocatario(id);
        return ResponseEntity.noContent().build();
    }


}
