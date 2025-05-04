# 📋 Ada Task Manager

API RESTful desenvolvida com Java e Spring Boot para gerenciamento de tarefas (to-do list). Este projeto foi criado como parte do programa da Ada Tech com foco na construção de APIs seguindo as boas práticas de arquitetura REST, separação de camadas e uso de DTOs.

---

## 🚀 Tecnologias utilizadas

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 Database (banco em memória)
- Maven
- Postman (testes manuais)

---

## 📌 Funcionalidades da API

- ✅ Criar uma tarefa (`POST /tasks`)
- ✅ Listar todas as tarefas (`GET /tasks`)
- ✅ Atualizar uma tarefa (`PUT /tasks/{id}`)
- ✅ Deletar uma tarefa (`DELETE /tasks/{id}`)

---

## 🛠️ Como executar o projeto

### Pré-requisitos

- Java 17 ou superior
- Maven instalado

### Instruções

```bash
# Clone o repositório
git clone https://github.com/guibonf1m/ada-task-manager.git

# Acesse a pasta do projeto
cd ada-task-manager

# Execute o projeto
./mvnw spring-boot:run
```

A aplicação estará rodando em:  
📎 `http://localhost:8080/tasks`

---

## 📫 Exemplos de uso (via Postman ou qualquer cliente HTTP)

### ▶ Criar uma tarefa (POST)

```
POST /tasks
Content-Type: application/json
```

```json
{
  "title": "Estudar Java",
  "description": "Aprender sobre Spring Boot",
  "deadline": "10/05/2025",
  "status": "PENDING"
}
```

---

### ▶ Listar todas as tarefas (GET)

```
GET /tasks
```

**Resposta esperada:**

```json
[
  {
    "id": 1,
    "title": "Estudar Java",
    "description": "Aprender sobre Spring Boot",
    "deadline": "10/05/2025",
    "status": "PENDING"
  }
]
```

---

### ▶ Atualizar uma tarefa (PUT)

```
PUT /tasks/1
Content-Type: application/json
```

```json
{
  "title": "Estudar Spring Boot",
  "description": "Atualizando dados da tarefa",
  "deadline": "15/05/2025",
  "status": "IN_PROGRESS"
}
```

---

### ▶ Deletar uma tarefa (DELETE)

```
DELETE /tasks/1
```

**Resposta esperada:**  
`204 No Content` (sem corpo de resposta)

---

## ✨ Melhorias futuras

- 🔍 Buscar tarefa por ID (`GET /tasks/{id}`)
- ✅ Validações de campos com `@Valid` (ex: `@NotBlank`, `@NotNull`)
- 📄 Documentação com Swagger/OpenAPI
- 🧪 Testes unitários e de integração com JUnit
- ☁️ Deploy na nuvem (Railway, Render, etc.)

---

## 👨‍💻 Autor

Desenvolvido por [Guilherme Bonfim](https://github.com/guibonf1m)

---

## 📄 Licença

Este projeto está licenciado sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
