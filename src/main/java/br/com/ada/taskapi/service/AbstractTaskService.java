package br.com.ada.taskapi.service;

import br.com.ada.taskapi.dto.TaskUpdateRequest;
import br.com.ada.taskapi.model.Task;
import br.com.ada.taskapi.repository.TaskRepository;

import java.util.Optional;

public abstract class AbstractTaskService implements TaskService {

    protected final TaskRepository taskRepository;

    public AbstractTaskService(TaskRepository taskRepository) {
        if (taskRepository == null) {
            throw new IllegalArgumentException("TaskRepository não pode ser nulo.");
        }
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        validate(task);
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    protected void validate(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Tarefa não pode ser nula.");
        }
    }

    @Override
    public Task updateStatus(Long id, Task.Status newStatus) {
        Task existingTask = getById(id);
        applyStatusUpdate(existingTask, newStatus);
        return save(existingTask);
    }

    protected void applyStatusUpdate(Task task, Task.Status newStatus) {
        task.setStatus(newStatus);
    }

    @Override
    public Task updateTask(TaskUpdateRequest updateRequest) {
        Task existingTask = getById(updateRequest.getId());

        if (updateRequest.getTitle() != null) {
            existingTask.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null) {
            existingTask.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getDeadline() != null) {
            existingTask.setDeadline(updateRequest.getDeadline());
        }
        if (updateRequest.getStatus() != null) {
            applyStatusUpdate(existingTask, updateRequest.getStatus());
        }

        validate(existingTask);
        return save(existingTask);
    }

    @Override
    public boolean deleteById(Long id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public void clearAll() {
        taskRepository.deleteAll();
    }

    @Override
    public void notifyUpcomingDeadlines(int daysBefore) {
        // Implementação futura para notificações com prazo configurável
    }
}
