###  LibFlow API - Sistema de Biblioteca e Locação

Este projeto é uma API REST moderna e robusta desenvolvida para gerenciar o fluxo completo de uma biblioteca, desde o cadastro de autores e livros até o controle de locatários e aluguéis. O foco principal da implementação é a **segurança dos dados**, **documentação automatizada** e **alta cobertura de testes**.

---

###  Tecnologias Utilizadas

O projeto foi construído utilizando o ecossistema Java de última geração em 2026:

*   **Java 21 (LTS):** Aproveitando Records, Virtual Threads e melhorias de performance.
*   **Spring Boot 3.5+:** Framework base para agilidade no desenvolvimento e deploy.
*   **PostgreSQL:** Banco de dados relacional de produção para persistência segura.
*   **Flyway Migration:** Gerenciamento versionado do esquema do banco de dados.
*   **Spring Data JPA (Hibernate 6.6):** Gerenciamento de persistência avançado.
*   **SpringDoc OpenAPI (Swagger):** Documentação interativa personalizada em `/docs`.
*   **JUnit 5 & MockMvc:** Suíte completa para testes de integração com rollback transacional.
*   **Lombok:** Redução de código boilerplate.
*   **Jakarta Validation:** Regras rigorosas de integridade de dados (Bean Validation).

---

### ️ Configuração de Ambiente

Para rodar o projeto localmente, configure as seguintes variáveis no seu `application.yaml` ou como variáveis de ambiente:

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `DB_URL` | URL JDBC de conexão com o PostgreSQL | `jdbc:postgresql://localhost:5432/bibliotecadb` |
| `DB_USERNAME` | Usuário do banco de dados | `postgres` |
| `DB_PASSWORD` | Senha do usuário do banco de dados | `sua_senha_aqui` |

> **Importante:** O **Flyway** criará as tabelas automaticamente na primeira execução. Certifique-se de que o banco `bibliotecadb` exista.

---

### Documentação da API (Swagger)

A API utiliza o **SpringDoc OpenAPI** para facilitar o consumo e o teste dos endpoints. Com a aplicação rodando, a documentação interativa pode ser acessada em:

👉 [http://localhost:8080/docs](http://localhost:8080/docs)

*(O caminho foi simplificado no `application.yaml` para melhor experiência do desenvolvedor).*

---

### 🚀 Implementações Técnicas e Diferenciais

#### 1. Evolução de Banco de Dados com Flyway
- As alterações de esquema são versionadas em scripts SQL em `src/main/resources/db/migration`.
- Garante que todos os ambientes (Dev, Test, Prod) estejam sempre com a mesma estrutura de tabelas.

#### 2. Injeção de Dependências e Arquitetura
- **Constructor Injection:** Uso estrito de `private final` e `@RequiredArgsConstructor`, eliminando o `@Autowired` em campos e garantindo que os serviços sejam instanciados com todas as suas dependências (Mappers, Repositories).
- **Mapeamento DTO:** Separação clara entre Entidades de banco de dados e objetos de transferência (Records), protegendo a camada de domínio.

#### 3. Testes de Integração Profissionais
- **Isolamento Transacional:** Uso de `@Transactional` nos testes para garantir que cada execução realize *rollback* automático, mantendo o banco limpo.
- **Validação de Contrato:** Uso de `jsonPath` para validar estruturas complexas, incluindo respostas paginadas.

#### 4. Paginação e Ordenação
Endpoints de listagem (`GET`) implementam `Pageable`, permitindo consultas eficientes:
- `GET /api/autores?page=0&size=10&sort=nome,asc`

---

### 📂 Como Executar

**Rodar a aplicação:**
```bash
./mvnw spring-boot:run




