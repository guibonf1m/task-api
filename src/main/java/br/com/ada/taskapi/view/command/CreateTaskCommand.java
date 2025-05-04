package br.com.ada.taskapi.view.command;

import br.com.ada.taskapi.controller.TaskController;
import br.com.ada.taskapi.model.Task;
import br.com.ada.taskapi.view.View;
import br.com.ada.taskapi.view.StatusViewHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class CreateTaskCommand implements Command {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final View view;
    private final TaskController taskController;

    public CreateTaskCommand(View view, TaskController taskController) {
        this.view = view;
        this.taskController = taskController;
    }

    @Override
    public void execute() {
        String title = view.getInput("📌 Informe o título da tarefa");
        String description = view.getInput("📝 Informe a descrição (opcional)");
        String deadlineStr = view.getInput("📅 Informe a data limite (DD/MM/YYYY)");
        String statusStr = view.getInput("🔄 Informe o status " + StatusViewHelper.getTaskAvailableStatus() + " (ou deixe em branco para 'Pendente')");

        try {
            LocalDate deadline = Optional.ofNullable(deadlineStr)
                    .filter(s -> !s.isBlank())
                    .map(date -> {
                        try {
                            return LocalDate.parse(date, FORMATTER);
                        } catch (DateTimeParseException e) {
                            throw new IllegalArgumentException("Formato de data inválido. Use DD/MM/YYYY.");
                        }
                    })
                    .orElseThrow(() -> new IllegalArgumentException("A data limite é obrigatória."));

            Task.Status status = Optional.ofNullable(statusStr)
                    .filter(s -> !s.isBlank())
                    .map(Task.Status::fromString)
                    .orElse(Task.Status.PENDENTE);

            Task task = taskController.createTask(title, description, deadline, status);
            view.showMessage("✅ Tarefa criada com sucesso!");
            view.showMessage(task.toString());
        } catch (IllegalArgumentException e) {
            view.showMessage("❌ Erro: " + e.getMessage());
        }
    }
}