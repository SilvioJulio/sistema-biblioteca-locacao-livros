package br.com.academia.bibliotecalocacao.config;


import br.com.academia.bibliotecalocacao.entity.Aluguel;
import br.com.academia.bibliotecalocacao.entity.Autor;
import br.com.academia.bibliotecalocacao.entity.Livro;
import br.com.academia.bibliotecalocacao.entity.Locatario;
import br.com.academia.bibliotecalocacao.repository.AluguelRepository;
import br.com.academia.bibliotecalocacao.repository.AutorRepository;
import br.com.academia.bibliotecalocacao.repository.LivroRepository;
import br.com.academia.bibliotecalocacao.repository.LocatarioRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor

public class DatabaseSeeder implements CommandLineRunner {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;
    private final LocatarioRepository locatarioRepository;
    private final AluguelRepository aluguelRepository;

    private final Faker faker = new Faker(new Locale("pt-BR"));

    @Override
    public void run(String... args) throws Exception {
        System.out.println(" Iniciando Seeder de Dados...");
        seedAutores();
        seedLivros();
        seedLocatarios();
        seedAlugueis();
        System.out.println(" Processo de Seeder Finalizado.");
    }

    private void seedAutores() {
        if (autorRepository.count() == 0) {
            System.out.println("🌱 Criando Autores...");
            for (int i = 0; i < 5; i++) {
                Autor a = new Autor();
                a.setNome(faker.name().fullName());
                a.setCpf(faker.number().digits(11));
                a.setSexo(faker.options().option("M", "F"));
                a.setAnoNascimento(faker.number().numberBetween(1950, 2000));
                autorRepository.save(a);
            }
        }
    }

    private void seedLivros() {
        if (livroRepository.count() == 0) {
            System.out.println("📚 Criando Livros...");
            var autores = autorRepository.findAll();
            if (autores.isEmpty()) return;

            for (int i = 0; i < 5; i++) {
                Livro l = new Livro();
                l.setNome(faker.book().title());
                l.setIsbn(faker.code().isbn13());
                l.setDataPublicacao(LocalDate.now().minusYears(2));
                l.setAutor(autores.get(0)); // Associa ao primeiro autor para teste rápido
                livroRepository.save(l);
            }
        }
    }

    private void seedLocatarios() {
        if (locatarioRepository.count() == 0) {
            System.out.println("👤 Criando Locatários...");
            for (int i = 0; i < 5; i++) {
                Locatario loc = new Locatario();
                loc.setNome(faker.name().fullName());
                loc.setCpf(faker.number().digits(11));
                loc.setEmail(faker.internet().emailAddress());
                loc.setTelefone("11999999999");
                loc.setDataNascimento(LocalDate.of(1990, 1, 1));
                locatarioRepository.save(loc);
            }
        }
    }


    private void seedAlugueis() {
        if (aluguelRepository.count() == 0) {
            System.out.println("📅 Populando Aluguéis...");

            List<Livro> livros = livroRepository.findAll();
            List<Locatario> locatarios = locatarioRepository.findAll();

            if (livros.isEmpty() || locatarios.isEmpty()) {
                System.out.println("⚠️ Não há livros ou locatários para criar aluguéis.");
                return;
            }

            for (int i = 0; i < 8; i++) {
                Aluguel aluguel = new Aluguel();

                // Seleciona um livro e um locatário aleatórios
                Livro livroSorteado = livros.get(faker.number().numberBetween(0, livros.size()));
                Locatario locatarioSorteado = locatarios.get(faker.number().numberBetween(0, locatarios.size()));

                aluguel.setLivro(livroSorteado);
                aluguel.setLocatario(locatarioSorteado);

                // Regra de Negócio: Data de retirada hoje e devolução em 7 dias
                aluguel.setDataRetirada(LocalDate.now());
                aluguel.setDataDevolucao(LocalDate.now().plusDays(7));

                aluguelRepository.save(aluguel);
            }
        }
    }
}


