package br.com.ada.taskapi.view.command;

import br.com.ada.taskapi.controller.TaskController;
import br.com.ada.taskapi.model.Task;
import br.com.ada.taskapi.view.View;
import br.com.ada.taskapi.view.StatusViewHelper;

public class UpdateTaskStatusCommand implements Command {

    private final View view;
    private final TaskController taskController;

    public UpdateTaskStatusCommand(View view, TaskController taskController) {
        this.view = view;
        this.taskController = taskController;
    }

    @Override
    public void execute() {
        Long id = view.getIntInput("📌 Informe o ID da tarefa para atualizar o status").longValue();
        String status = view.getInput("🔄 Novo status (" + StatusViewHelper.getTaskAvailableStatus() + ")");

        try {
            Task updatedTask = taskController.updateTaskStatus(id, Task.Status.fromString(status));
            view.showMessage("✅ Status atualizado com sucesso!");
            view.showMessage(updatedTask.toString());
        } catch (IllegalArgumentException e) {
            view.showMessage("❌ Erro: " + e.getMessage());
        }
    }
}
