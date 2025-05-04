package br.com.ada.taskapi.repository;

import br.com.ada.taskapi.model.Task;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// comitar no repositorio banco-de-dados-em-arquivo

@Repository
@Primary
public class TaskRepositoryJsonFileImpl implements TaskRepository {

    public static final int INITIAL_COUNTER = 1;
    public static final int ID_INCREMENT = 1;
    private static final Path databaseDir = Path.of("database");
    private static final Path taskFile = Path.of("database/tasks.json");
    private static final Path counterFile = Path.of("database/taskCounter.txt");
    private static final TaskRepositoryJsonFileImpl INSTANCE = new TaskRepositoryJsonFileImpl();
    private final ObjectMapper mapper;

    private TaskRepositoryJsonFileImpl() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        if (!Files.exists(databaseDir)) {
            try {
                Files.createDirectory(databaseDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static TaskRepositoryJsonFileImpl getInstance() {
        return INSTANCE;
    }

    private int getCurrentCount() {
        try {
            if (Files.exists(counterFile)) {
                return Integer.parseInt(Files.readString(counterFile));
            } else {
                Files.writeString(counterFile, String.valueOf(INITIAL_COUNTER));
                return INITIAL_COUNTER;
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler contador de IDs", e);
        }
    }

    private long getIncrementalId() {
        int current = getCurrentCount();
        int next = current + ID_INCREMENT;
        try {
            Files.writeString(counterFile, String.valueOf(next), StandardOpenOption.TRUNCATE_EXISTING);
            return current;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao atualizar contador de IDs", e);
        }
    }

    private List<Task> loadTasks() {
        try {
            if (!Files.exists(taskFile)) return new ArrayList<>();
            String json = Files.readString(taskFile);
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar tarefas", e);
        }
    }

    private void saveTasks(List<Task> tasks) {
        try {
            String json = mapper.writeValueAsString(tasks);
            Files.writeString(taskFile, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar tarefas", e);
        }
    }

    @Override
    public Task save(Task task) {
        List<Task> tasks = loadTasks();
        if (task.getId() == null) {
            task.setId(getIncrementalId());
        } else {
            tasks.removeIf(t -> t.getId().equals(task.getId()));
        }
        tasks.add(task);
        saveTasks(tasks);
        return task;
    }

    @Override
    public List<Task> findAll() {
        return loadTasks();
    }

    @Override
    public List<Task> findByStatus(Task.Status status) {
        return loadTasks().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findBy(Predicate<Task> predicate) {
        return loadTasks().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Task> findById(Long id) {
        return loadTasks().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean deleteById(Long id) {
        List<Task> tasks = loadTasks();
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));
        if (removed) saveTasks(tasks);
        return removed;
    }

    @Override
    public void deleteAll() {
        saveTasks(new ArrayList<>());
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}
