package br.com.ada.taskapi.service;

import br.com.ada.taskapi.dto.TaskUpdateRequest;
import br.com.ada.taskapi.model.Task;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface TaskService {

    Task save(Task task);

    List<Task> findAll(Optional<Comparator<Task>> orderBy);

    List<Task> findByStatus(Task.Status status, Optional<Comparator<Task>> orderBy);

    List<Task> findBy(Predicate<Task> predicate, Optional<Comparator<Task>> orderBy);

    Optional<Task> findById(Long id);

    default Task getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));
    }

    boolean deleteById(Long id);

    void clearAll();

    void notifyUpcomingDeadlines(int daysBefore);

    Task createTask(String title, String description, String deadlineStr, Task.Status status);

    Task updateTask(TaskUpdateRequest updateRequest);

    Task updateStatus(Long id, Task.Status newStatus);

    Optional<Task> update(Long id, Task task);

    // Se quiser evitar duplicação:
    default boolean delete(Long id) {
        return deleteById(id);
    }
}
