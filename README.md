
### LibFlow API - Sistema de Biblioteca e Locação

Este projeto é uma API REST moderna e robusta desenvolvida para gerenciar o fluxo completo de uma biblioteca, desde o cadastro de autores e livros até o controle de locatários e aluguéis. O foco principal da implementação é a **segurança dos dados**, **documentação automatizada** e **alta cobertura de testes**.

---

### Tecnologias Utilizadas

O projeto foi construído utilizando o ecossistema Java de última geração em 2026:

*   **Java 21 (LTS):** Aproveitando Records, Virtual Threads e melhorias de performance.
*   **Spring Boot 3.5+:** Framework base para agilidade no desenvolvimento e deploy.
*   **PostgreSQL:** Banco de dados relacional de produção para persistência segura.
*   **Spring Data JPA (Hibernate 6.6):** Gerenciamento de persistência com mapeamento objeto-relacional avançado.
*   **SpringDoc OpenAPI (Swagger):** Documentação interativa personalizada e acessível.
*   **JUnit 5 & MockMvc:** Suíte completa para testes de integração com rollback transacional.
*   **Lombok:** Redução de código boilerplate com foco em legibilidade.
*   **Jakarta Validation:** Regras rigorosas de integridade de dados (Bean Validation).

---

### Configuração de Ambiente

Para rodar o projeto localmente, você deve configurar as seguintes variáveis de ambiente na sua IDE (IntelliJ/VSCode) ou sistema operacional:

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `DB_URL` | URL JDBC de conexão com o PostgreSQL | `jdbc:postgresql://localhost:5432/bibliotecadb` |
| `DB_USERNAME` | Usuário do banco de dados | `postgres` |
| `DB_PASSWORD` | Senha do usuário do banco de dados | `sua_senha_aqui` |

> **Nota:** Certifique-se de que o banco de dados `bibliotecadb` foi criado previamente no PostgreSQL antes de iniciar a aplicação.

---

### Documentação da API (Swagger)

A API utiliza o **SpringDoc OpenAPI** para facilitar o consumo e o teste dos endpoints. Com a aplicação rodando, a documentação pode ser acessada em:

👉 [http://localhost:8080/docs](http://localhost:8080/docs)

*(O caminho foi simplificado para `/docs` para melhor experiência do desenvolvedor).*

---

### Implementações Técnicas e Diferenciais

### 1. Injeção de Dependências e Arquitetura
- **Constructor Injection:** Abolimos o uso de `@Autowired` em campos de classe. Utilizamos `private final` e `@RequiredArgsConstructor`, garantindo imutabilidade e tornando o sistema resiliente a `NullPointerException`.
- **Organização RESTful:** Endpoints padronizados sob o prefixo `/api/` com uso correto dos métodos HTTP (GET, POST, PUT, DELETE).

### 2. Validações e Integridade
- **Bean Validation:** Uso de `@NotBlank`, `@CPF`, `@Past`, e `@Pattern` para garantir que apenas dados válidos entrem no sistema.
- **Relacionamentos:** Mapeamento bidirecional `@OneToMany` e `@ManyToOne` configurado para manter a integridade referencial.

### 3. Testes de Integração Profissionais
- **Isolamento Transacional:** Os testes de integração utilizam a anotação `@Transactional`, o que garante que cada teste execute em uma transação limpa, realizando *rollback* automático ao final.
- **MockMvc:** Simulação de requisições HTTP reais com validação de `jsonPath` para garantir que o contrato da API nunca seja quebrado.

### 4. Paginação e Ordenação Dinâmica
Todos os endpoints de listagem suportam paginação via parâmetros de consulta:
- `page`: Número da página (inicia em 0).
- `size`: Quantidade de registros por página.
- `sort`: Campo e direção da ordenação (ex: `nome,desc`).

---

### 📂 Como Executar os Testes

Para rodar a suíte completa de testes de integração via terminal:

```bash
./mvnw test




