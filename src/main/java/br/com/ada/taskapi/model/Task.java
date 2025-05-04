package br.com.ada.taskapi.model;

import br.com.ada.taskapi.model.Task;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private Status status;

    public Task(Long id, String title, String description, LocalDate deadline, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }

    public Task() {
    }

    public enum Status {
        PENDENTE("Pendente"),
        EM_ANDAMENTO("Em andamento"),
        BLOQUEADO("Bloqueado"),
        CONCLUIDO("Concluído");

        private final String descricao;

        Status(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        public static Status fromString(String status) {
            for (Status s : values()) {
                if (s.descricao.equalsIgnoreCase(status)) {
                    return s;
                }
            }
            throw new IllegalArgumentException("Status inválido: " + status);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
                "📌 Tarefa #%d%nTítulo: %s%nDescrição: %s%n📅 Prazo: %s%n🔄 Status: %s%n",
                id, title, description, deadline, status.getDescricao()
        );
    }

    public String toString(String pattern) {
        return String.format(
                "📌 Tarefa #%d%nTítulo: %s%nDescrição: %s%n📅 Prazo: %s%n🔄 Status: %s%n",
                id, title, description, DateTimeFormatter.ofPattern(pattern).format(deadline), status.getDescricao()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
