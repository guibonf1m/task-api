package br.com.ada.taskapi.controller;

import br.com.ada.taskapi.dto.TaskRequest;
import br.com.ada.taskapi.model.Task;
import br.com.ada.taskapi.repository.TaskRepository;
import br.com.ada.taskapi.service.TaskService;
import br.com.ada.taskapi.service.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class TaskControllerImpl extends AbstractTaskController implements TaskController {

    @Autowired
    private TaskRepository taskRepository;


    private static final int TAMANHO_MINIMO_TITULO = 3;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final Predicate<String> IS_INVALID_TITLE =
            s -> s == null || s.isBlank() || s.length() < TAMANHO_MINIMO_TITULO;

    private static final Predicate<String> IS_BLANK = s -> s == null || s.isBlank();

    public static final int MIN_DESCRIPTION_LENGTH = 5;
    private static final Predicate<String> IS_INVALID_DESCRIPTION_TEXT = s -> s != null && !s.isBlank() && s.length() < MIN_DESCRIPTION_LENGTH;

    private static final Predicate<LocalDate> IS_BEFORE_TODAY =
            date -> date.isBefore(LocalDate.now());

    private final TaskService taskService;
    private final TaskServiceImpl taskServiceImpl;

    public TaskControllerImpl(TaskService taskService, TaskServiceImpl taskServiceImpl) {
        super(taskService);
        this.taskService = taskService;
        this.taskServiceImpl = taskServiceImpl;
    }

    @Override
    protected void validateTitle(String title) {
        Optional.ofNullable(title)
                .filter(IS_INVALID_TITLE.negate())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Título deve ter no mínimo " + TAMANHO_MINIMO_TITULO + " caracteres"
                ));
    }

    @Override
    protected void validateDeadline(String deadline) {
        Optional.ofNullable(deadline)
                .map(date -> {
                    try {
                        return LocalDate.parse(date, FORMATTER);
                    } catch (DateTimeParseException e) {
                        throw new IllegalArgumentException("Formato de data inválido. Use dd/MM/yyyy.");
                    }
                })
                .filter(IS_BEFORE_TODAY.negate())
                .orElseThrow(() -> new IllegalArgumentException("Data deve ser igual ou superior à data atual."));
    }

    @Override
    protected void validateStatus(Task.Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo.");
        }

        if (status == Task.Status.CONCLUIDO) {
            throw new IllegalArgumentException("Uma nova tarefa não pode ser criada com o status 'CONCLUÍDO'.");
        }
    }

    @Override
    public Task createTask(String title, String description, LocalDate deadline, Task.Status status) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id,
                           String title,
                           String description,
                           String deadlineStr,
                           Task.Status status) {

        LocalDate deadline = LocalDate.parse(deadlineStr, FORMATTER);

        // Monta um objeto Task “vazio” só com os novos valores
        Task toUpdate = new Task();
        toUpdate.setTitle(title);
        toUpdate.setDescription(description);
        toUpdate.setDeadline(deadline);
        toUpdate.setStatus(status);

        // Chama o service, que devolve Optional<Task>
        return taskService.update(id, toUpdate)
                // Se não existir, lança exceção que o controller REST captura e vira 404
                .orElseThrow(() -> new IllegalArgumentException("Task com id " + id + " não encontrada"));
    }

    @Override
    public boolean deleteTask(Long id){
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Task updateTaskStatus(Long id, Task.Status newStatus) {
        return null;
    }

    @Override
    public List<Task> getAllTasks(Optional<Comparator<Task>> orderBy) {
        List<Task> tasks = taskRepository.findAll();

        return tasks;
    }

    @Override
    public List<Task> getTasksByStatus(Task.Status status, Optional<Comparator<Task>> orderBy) {
        return List.of();
    }

    @Override
    public List<Task> getTasksBy(Predicate<Task> predicate, Optional<Comparator<Task>> orderBy) {
        return List.of();
    }

    @Override
    public List<Task> getTasks() {
        return List.of();
    }
}