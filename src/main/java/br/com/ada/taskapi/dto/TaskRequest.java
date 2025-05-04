package br.com.ada.taskapi.dto;

import br.com.ada.taskapi.model.Task;

public class TaskRequest {

    private String title;
    private String description;
    private String deadline; // no formato dd/MM/yyyy
    private Task.Status status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Task.Status getStatus() {
        return status;
    }

    public void setStatus(Task.Status status) {
        this.status = status;
    }
}

