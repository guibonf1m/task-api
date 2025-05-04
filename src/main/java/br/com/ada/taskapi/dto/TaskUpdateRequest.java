package br.com.ada.taskapi.dto;

import br.com.ada.taskapi.model.Task;

import java.time.LocalDate;

public class TaskUpdateRequest {
    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private Task.Status status;

    private TaskUpdateRequest() {}

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public Task.Status getStatus() { return status; }

    public static Builder builder(Long id) {
        return new Builder(id);
    }

    public static class Builder {
        private final TaskUpdateRequest instance;

        private Builder(Long id) {
            this.instance = new TaskUpdateRequest();
            this.instance.id = id;
        }

        public Builder title(String title) {
            this.instance.title = title;
            return this;
        }

        public Builder description(String description) {
            this.instance.description = description;
            return this;
        }

        public Builder deadline(LocalDate deadline) {
            this.instance.deadline = deadline;
            return this;
        }

        public Builder status(Task.Status status) {
            this.instance.status = status;
            return this;
        }

        public TaskUpdateRequest build() {
            return instance;
        }
    }
}
