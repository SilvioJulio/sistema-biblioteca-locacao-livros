
## 📚 Sistema de Biblioteca e Locação (API)

Este projeto é uma API REST robusta desenvolvida para gerenciar o aluguel de livros em uma biblioteca. O foco principal da implementação foi a **segurança de dados** e a **cobertura de testes de integração**.


### 💻 Tecnologias Utilizadas

O projeto foi desenvolvido utilizando o que há de mais moderno no ecossistema Java em 2026:

*   **Java 21 (LTS):** Utilização de Records, Sealed Classes e as últimas melhorias de performance da JVM.
*   **Spring Boot 3.4.1:** Framework base para a construção da API, facilitando a configuração e o deploy.
*   **Spring Data JPA (Hibernate):** Abstração de persistência para facilitar o gerenciamento e as consultas no banco de dados.
*   **Datafaker:** Biblioteca sucessora do JavaFaker, utilizada para gerar massas de dados realistas (nomes, CPFs e títulos) automaticamente.
*   **SpringDoc OpenAPI (Swagger):** Documentação interativa da API, acessível via interface gráfica para testes rápidos.
*   **H2 Database:** Banco de dados em memória utilizado para agilizar o ciclo de desenvolvimento e os testes de integração.
*   **JUnit 5 & Mockito:** Conjunto de ferramentas essencial para a implementação de testes unitários e de integração robustos.


###  Documentação da API (Swagger)

Com a aplicação rodando, você pode acessar a documentação interativa, testar os endpoints e visualizar os esquemas de dados através do endereço:

👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

##  Implementações Técnicas

### 1. Arquitetura e Injeção de Dependências
- **Injeção por Construtor:** Implementada em todos os serviços (como `AluguelService`) utilizando a anotação `@RequiredArgsConstructor` do Lombok em atributos `final`. Isso garante que a aplicação seja resiliente a `NullPointerException` e facilite testes unitários.
- **Mapeamento de Rotas:** Organização de endpoints sob o prefixo `/api/` (ex: `/api/alugueis`), seguindo padrões RESTful.

### 2. Validações Rigorosas (Bean Validation)
A aplicação impede a persistência de dados malformados através de regras no nível de entidade:
- **CPF:** Validação via Regex para aceitar exatamente **11 dígitos numéricos** (sem pontos ou traços).
- **ISBN:** Restrição de unicidade para evitar livros duplicados.
- **Campos Obrigatórios:** Uso de `@NotBlank` e `@NotNull` para Email, Telefone, Sexo e Datas, garantindo que o banco de dados nunca contenha informações incompletas.

### 3. Testes de Integração Automatizados
Desenvolvida uma suíte de testes completa utilizando **JUnit 5**, **MockMvc** e **AssertJ**:
- **Cenários de Sucesso:** Testes de fluxo completo de criação (POST), busca (GET), atualização (PUT) e deleção (DELETE).
- **MockMvc:** Simulação de requisições HTTP reais com validação de códigos de status (201 Created, 204 No Content, 200 OK, 404 Not Found).
- **Setup de Banco:** Implementado método `@BeforeEach` para limpeza e reinicialização do banco de dados (H2/Testcontainers), garantindo a independência entre os testes.

### 4. Paginação e Ordenação
- Implementado suporte a **Spring Data Pageable** em todos os endpoints de listagem.
- Os resultados são retornados em formato de objeto `Page`, contendo metadados como `totalElements`, `totalPages` e `content`.

---
### Como testar a Paginação no Postman

A API utiliza o padrão de paginação dinâmica do **Spring Data JPA**. Isso permite que grandes volumes de dados sejam transferidos em pequenas "fatias" (páginas), otimizando a performance do sistema.

###  Parâmetros de Consulta (Query Params)
Para filtrar a visualização, adicione os seguintes parâmetros na URL do Postman:

| Parâmetro | Descrição | Valor Padrão | Exemplo |
| :--- | :--- | :--- | :--- |
| `page` | Índice da página que deseja visualizar (**inicia em 0**) | `0` | `page=1` (Página 2) |
| `size` | Quantidade de registros exibidos por página | `20` | `size=5` |
| `sort` | Campo para ordenação e direção (`asc` ou `desc`) | `id,asc` | `sort=id,desc` |

#### 🔗 Exemplo de URL Combinada:
Para buscar a **primeira página**, com apenas **5 registros**, ordenando pelos **mais recentes**, utilize:
```text
GET => http://localhost:8080/api/alugueis?page=0&size=5&sort=id,desc




