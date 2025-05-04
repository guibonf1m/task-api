package br.com.ada.taskapi.controller;

import br.com.ada.taskapi.dto.TaskRequest;
import br.com.ada.taskapi.dto.TaskResponse;
import br.com.ada.taskapi.model.Task;
import br.com.ada.taskapi.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractTaskController implements TaskController {

    protected final TaskService taskService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected AbstractTaskController(TaskService taskService) {
        if (taskService == null) {
            throw new IllegalArgumentException("TaskService não pode ser nulo.");
        }
        this.taskService = taskService;
    }

    // Métodos abstratos para validação
    protected abstract void validateTitle(String title);
    protected abstract void validateDeadline(String deadline);
    protected abstract void validateStatus(Task.Status status);

    @GetMapping
    public List<TaskResponse> findAll() {
        return taskService.findAll(Optional.empty()).stream()
                .map(TaskResponse::fromTask)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable Long id) {
        return taskService.findById(id)
                .map(TaskResponse::fromTask)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest request) {
        validateTitle(request.getTitle());
        validateDeadline(request.getDeadline());
        validateStatus(request.getStatus());

        LocalDate deadline = LocalDate.parse(request.getDeadline(), FORMATTER);
        Task task = new Task(null, request.getTitle(), request.getDescription(), deadline, request.getStatus());
        Task saved = taskService.save(task);

        return ResponseEntity.ok(TaskResponse.fromTask(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @RequestBody TaskRequest request) {
        validateTitle(request.getTitle());
        validateDeadline(request.getDeadline());
        validateStatus(request.getStatus());

        LocalDate deadline = LocalDate.parse(request.getDeadline(), FORMATTER);
        Task task = new Task(id, request.getTitle(), request.getDescription(), deadline, request.getStatus());

        Optional<Task> updated = taskService.update(id, task);
        return updated.map(TaskResponse::fromTask)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = taskService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

