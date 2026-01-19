package br.com.academia.bibliotecalocacao.service;


import br.com.academia.bibliotecalocacao.dtos.response.AluguelResponse;
import br.com.academia.bibliotecalocacao.dtos.request.AluguelRequest;
import br.com.academia.bibliotecalocacao.entity.Aluguel;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.mapper.AlugueMapper;
import br.com.academia.bibliotecalocacao.repository.AluguelRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LocatarioRepository locatarioRepository;


    public AluguelResponse criar(AluguelRequest aluguelRequest) {
        Livro livro = livroRepository.findById(aluguelRequest.livroId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + aluguelRequest.livroId()));


        Locatario locatario = locatarioRepository.findById(aluguelRequest.locatarioId())
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado com "));


       Aluguel aluguel = AlugueMapper.toEntity(livro, locatario);

        Aluguel salvo = aluguelRepository.save(aluguel);

        return AlugueMapper.toDTO(salvo);
    }

    public Page<AluguelResponse> listarTodos(Pageable pageable) {
        return aluguelRepository.findAll(pageable)
                .map(AlugueMapper::toDTO);
    }

    public AluguelResponse buscarPorId(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado com ID: " + id));

        return AlugueMapper.toDTO(aluguel);
    }

    public AluguelResponse atualizar(Long id, AluguelRequest aluguelRequest) {
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

        return AlugueMapper.toDTO(atualizado);
    }


    public void deletar(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado com ID: " + id));

        aluguelRepository.delete(aluguel);
    }
}

