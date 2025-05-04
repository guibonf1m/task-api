package br.com.ada.taskapi.service;

import br.com.ada.taskapi.model.Task;
import br.com.ada.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class TaskServiceImpl extends AbstractTaskService {

    public static final Comparator<Task> DEFAULT_TASK_SORT = Comparator.comparing(Task::getDeadline);
    private static final int MIN_TITLE_LENGTH = 3;

    public TaskServiceImpl(TaskRepository taskRepository) {
        super(taskRepository);
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank() || title.length() < MIN_TITLE_LENGTH) {
            throw new IllegalArgumentException("Título inválido: '" + title + "'. Deve ter pelo menos " + MIN_TITLE_LENGTH + " caracteres.");
        }
    }

    private static void validateDeadline(LocalDate deadline) {
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data inválida: '" + deadline + "'. Deve ser maior ou igual à data atual.");
        }
    }

    private static void validateStatus(Task.Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status inválido: não pode ser nulo.");
        }

        if (status == Task.Status.CONCLUIDO) {
            throw new IllegalArgumentException("Uma nova tarefa não pode ser criada com o status 'CONCLUÍDO'.");
        }
    }

    @Override
    public Task createTask(String title, String description, String deadlineStr, Task.Status status) {
        validateTitle(title);
        validateStatus(status);

        LocalDate deadline = LocalDate.parse(deadlineStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        validateDeadline(deadline);

        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setDeadline(deadline);
        newTask.setStatus(status);

        return taskRepository.save(newTask);
    }

    @Override
    public List<Task> findAll(Optional<Comparator<Task>> orderBy) {
        return taskRepository.findAll().stream()
                .sorted(orderBy.orElse(DEFAULT_TASK_SORT))
                .toList();
    }

    @Override
    public List<Task> findByStatus(Task.Status status, Optional<Comparator<Task>> orderBy) {
        return taskRepository.findByStatus(status).stream()
                .sorted(orderBy.orElse(DEFAULT_TASK_SORT))
                .toList();
    }

    @Override
    public List<Task> findBy(Predicate<Task> predicate, Optional<Comparator<Task>> orderBy) {
        var stream = taskRepository.findAll().stream()
                .filter(predicate);

        if (orderBy.isPresent()) {
            stream = stream.sorted(orderBy.get());
        }

        return stream.toList();
    }

    @Override
    public void validate(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task não pode ser nula");
        }

        validateTitle(task.getTitle());
        validateDeadline(task.getDeadline());
        validateStatus(task.getStatus());
    }

    @Override
    public Optional<Task> update(Long id, Task task) {
        return taskRepository.findById(id).map(existingTask -> {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setDeadline(task.getDeadline());
            existingTask.setStatus(task.getStatus());
            return taskRepository.save(existingTask);
        });
    }

    @Override
    public boolean delete(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
