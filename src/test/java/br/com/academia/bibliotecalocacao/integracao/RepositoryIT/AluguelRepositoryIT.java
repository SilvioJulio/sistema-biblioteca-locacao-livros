package br.com.academia.bibliotecalocacao.integracao.RepositoryIT;


import br.com.academia.bibliotecalocacao.entity.Aluguel;
import br.com.academia.bibliotecalocacao.entity.Autor;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.repository.AluguelRepository;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class AluguelRepositoryIT {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LocatarioRepository locatarioRepository;

    private Livro livroSalvo;
    private Locatario locatarioSalvo;

    private final Faker faker = new Faker(new Locale("pt-BR"));

    @BeforeEach
    void setUp() {

        aluguelRepository.deleteAll();
        livroRepository.deleteAll();
        locatarioRepository.deleteAll();
        autorRepository.deleteAll();



        Autor autor = new Autor();
        autor.setNome(faker.book().author()); // Nome de autor real
        autor.setSexo(faker.gender().types());
        autor.setCpf(faker.cpf().valid(false)); // Gera CPF válido sem pontos
        autor.setAnoNascimento(faker.number().numberBetween(1850, 2000));
        autor = autorRepository.save(autor);


        Livro livro = new Livro();
        livro.setNome(faker.book().title());
        livro.setIsbn(faker.code().isbn13());
        livro.setDataPublicacao(LocalDate.now().minusYears(faker.number().numberBetween(1, 50)));
        livro.setAutor(autor);
        livroSalvo = livroRepository.save(livro);


        Locatario loc = new Locatario();
        loc.setNome(faker.name().fullName());
        loc.setCpf(faker.cpf().valid(false)); // Garante um CPF diferente do autor
        loc.setEmail(faker.internet().emailAddress());
        loc.setTelefone(faker.phoneNumber().cellPhone());
        loc.setSexo(faker.gender().binaryTypes());
        loc.setDataNascimento(LocalDate.now().minusYears(faker.number().numberBetween(18, 70)));
        locatarioSalvo = locatarioRepository.save(loc);
    }

    @Test
    @DisplayName("Deve encontrar livros alugados com data de devolução futura")
    void deveEncontrarLivrosAlugados() {
        // Arrange: Aluguel com devolução amanha
        Aluguel aluguel = new Aluguel();
        aluguel.setLivro(livroSalvo);
        aluguel.setLocatario(locatarioSalvo);
        aluguel.setDataRetirada(LocalDate.now());
        aluguel.setDataDevolucao(LocalDate.now().plusDays(1));
        aluguelRepository.save(aluguel);


        List<Livro> livros = aluguelRepository.findLivrosAlugados();

        assertThat(livros).isNotEmpty();


        assertThat(livros.get(0).getNome()).isEqualTo(livroSalvo.getNome());

    }
    @Test
    @DisplayName("Deve verificar se existe aluguel por ID do Livro")
    void deveExistirPorLivroId() {
        Aluguel aluguel = new Aluguel();
        aluguel.setLivro(livroSalvo);
        aluguel.setLocatario(locatarioSalvo);
        aluguel.setDataRetirada(LocalDate.now());
        aluguel.setDataDevolucao(LocalDate.now());
        aluguelRepository.save(aluguel);

        boolean existe = aluguelRepository.existsByLivroId(livroSalvo.getId());

        assertThat(existe).isTrue();
    }

}



