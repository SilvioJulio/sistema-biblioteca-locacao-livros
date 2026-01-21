package br.com.academia.bibliotecalocacao.controller;

import br.com.academia.bibliotecalocacao.dtos.request.LocatarioRequest;
import br.com.academia.bibliotecalocacao.dtos.response.LocatarioResponse;
import br.com.academia.bibliotecalocacao.service.LocatarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<LocatarioResponse> locatarioPorId(@PathVariable Long id){
        return ResponseEntity.ok(locatarioService.buscarLocatarioPorId(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<LocatarioResponse> atualizar(@PathVariable Long id, @RequestBody LocatarioResponse request){
        return ResponseEntity.ok(locatarioService.atualizarLocatario(id,request));

    }

    @GetMapping
    public ResponseEntity<List<LocatarioResponse>> listarTodos(){
        return ResponseEntity.ok(locatarioService.listarTodosLocatarios());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<LocatarioResponse> deletar (@PathVariable Long id){
        locatarioService.deletarLocatario(id);
        return ResponseEntity.noContent().build();
    }

}
