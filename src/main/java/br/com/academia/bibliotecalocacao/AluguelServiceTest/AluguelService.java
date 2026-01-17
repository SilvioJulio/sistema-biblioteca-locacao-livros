package br.com.academia.bibliotecalocacao.AluguelServiceTest;


import br.com.academia.bibliotecalocacao.dtos.AluguelDTO;
import br.com.academia.bibliotecalocacao.dtos.AluguelRequestDTO;
import br.com.academia.bibliotecalocacao.entity.Aluguel;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.repository.AluguelRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LocatarioRepository locatarioRepository;


    public AluguelDTO criar(AluguelRequestDTO aluguelRequest) {
        Livro livro = livroRepository.findById(aluguelRequest.livroId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + aluguelRequest.livroId()));


        Locatario locatario = locatarioRepository.findById(aluguelRequest.locatarioId())
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado com "));


      Aluguel aluguel = new Aluguel();
        aluguel.setLivro(livro);
        aluguel.setLocatario(locatario);
        aluguel.setDataRetirada(java.time.LocalDate.now());
        aluguel.setDataDevolucao(java.time.LocalDate.now().plusDays(2)); // Exemplo: devolução em 7 dias

        Aluguel salvo = aluguelRepository.save(aluguel);

        return new AluguelDTO(
                salvo.getId(),
                salvo.getDataRetirada(),
                salvo.getDataDevolucao(),
                livro.getId(),
                livro.getNome(),
                locatario.getId(),
                locatario.getNome()
        );

    }

    public Page<AluguelDTO> listarTodos (Pageable pageable){
        return  aluguelRepository.findAll((Sort) pageable).stream().map(aluguel -> )
    }

    public AluguelDTO buscarPorId(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado com ID: " + id));

        Livro livro = aluguel.getLivro();
        Locatario locatario = aluguel.getLocatario();

        return new AluguelDTO(
                aluguel.getId(),
                aluguel.getDataRetirada(),
                aluguel.getDataDevolucao(),
                livro.getId(),
                livro.getNome(),
                locatario.getId(),
                locatario.getNome()
        );
    }

    public AluguelDTO atualizar(Long id, AluguelRequestDTO aluguelRequest) {
        Aluguel aluguelExistente = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado com ID: " + id));

        Livro livro = livroRepository.findById(aluguelRequest.livroId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + aluguelRequest.livroId()));

        Locatario locatario = locatarioRepository.findById(aluguelRequest.locatarioId())
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado com ID: " + aluguelRequest.locatarioId()));

        aluguelExistente.setLivro(livro);
        aluguelExistente.setLocatario(locatario);
        // Atualize outras propriedades conforme necessário

        Aluguel atualizado = aluguelRepository.save(aluguelExistente);

        return new AluguelDTO(
                atualizado.getId(),
                atualizado.getDataRetirada(),
                atualizado.getDataDevolucao(),
                livro.getId(),
                livro.getNome(),
                locatario.getId(),
                locatario.getNome()
        );
    }


    public void deletar(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado com ID: " + id));

        aluguelRepository.delete(aluguel);
    }
}

