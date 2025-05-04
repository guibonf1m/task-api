package br.com.ada.taskapi.repository;

import br.com.ada.taskapi.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface TaskRepository {

    Task save(Task task);

    List<Task> findAll();

    List<Task> findByStatus(Task.Status status);

    List<Task> findBy(Predicate<Task> predicate);

    Optional<Task> findById(Long id);

    boolean deleteById(Long id);

    void deleteAll();

    boolean existsById(Long id);
}
