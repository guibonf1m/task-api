package br.com.ada.taskapi.controller;

import br.com.ada.taskapi.dto.TaskRequest;
import br.com.ada.taskapi.dto.TaskResponse;
import br.com.ada.taskapi.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController // Define que esta classe é um controller REST
@RequestMapping("/tasks") // Prefixo das rotas da API
public class TaskRestController {

    private final TaskControllerImpl taskController;

    // Injeção de dependência via construtor
    public TaskRestController(TaskControllerImpl taskController) {
        this.taskController = taskController;
    }

    @PostMapping // Rota POST para criar uma nova tarefa
    public ResponseEntity<Task> create(@RequestBody TaskRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate deadline = LocalDate.parse(request.getDeadline(), formatter);

        Task created = taskController.createTask(
                request.getTitle(),
                request.getDescription(),
                deadline,
                request.getStatus()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(created); // HTTP 201
    }

    @GetMapping // Rota GET  para listar todas as tarefas
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<Task> tasks = taskController.getAllTasks(Optional.empty()); // Busca todas as tarefas
        List<TaskResponse> response = tasks.stream()
                .map(TaskResponse::fromTask) // Converte para DTO de saída
                .toList();

        return ResponseEntity.ok(response); // HTTP 200
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @RequestBody TaskRequest request) {
        try {

            Task updated = taskController.updateTask(
                    id,
                    request.getTitle(),
                    request.getDescription(),
                    request.getDeadline(),
                    request.getStatus()
            );

            if (updated == null) {
                return ResponseEntity.notFound().build();
            }

            TaskResponse response = TaskResponse.fromTask(updated); // Conversão do Task para TaskResponse
            return ResponseEntity.ok(response); // Retorna o DTO correto

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Tarefa não encontrada
        }
    }

    @DeleteMapping("/{id}") // Rota DELETE para excluir uma tarefa
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = taskController.deleteTask(id); // Tenta excluir a tarefa

        if (deleted) {
            return ResponseEntity.noContent().build(); // HTTP 204
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }
}

