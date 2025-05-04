package br.com.ada.taskapi.view.command;

import br.com.ada.taskapi.controller.TaskController;
import br.com.ada.taskapi.model.Task;
import br.com.ada.taskapi.service.TaskComparators;
import br.com.ada.taskapi.view.View;
import br.com.ada.taskapi.view.StatusViewHelper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FilterTasksByStatusCommand implements Command {

    private final View view;
    private final TaskController taskController;

    public FilterTasksByStatusCommand(View view, TaskController taskController) {
        this.view = view;
        this.taskController = taskController;
    }

    @Override
    public void execute() {
        String statusInput = view.getInput("🔎 Digite o status para filtrar (" + StatusViewHelper.getTaskAvailableStatus() + ")");
        Task.Status status;
        try {
            status = Task.Status.fromString(statusInput);
        } catch (IllegalArgumentException e) {
            view.showMessage("❌ Status inválido. Tente novamente.");
            return;
        }

        Optional<Comparator<Task>> orderBy = getSortingMethod();
        List<Task> tasks = taskController.getTasksByStatus(status, orderBy);

        if (tasks.isEmpty()) {
            view.showMessage("📭 Nenhuma tarefa encontrada com esse status.");
        } else {
            tasks.forEach(task -> view.showMessage(task.toString()));
        }
    }

    private Optional<Comparator<Task>> getSortingMethod() {
        view.showMessage("Escolha o critério de ordenação:");
        view.showMessage("1 - Por Data Limite");
        view.showMessage("2 - Por Título");
        view.showMessage("3 - Por Status");
        view.showMessage("4 - Sem ordenação");

        int option = view.getIntInput("Digite o número da opção");

        if (option == 4) {
            return Optional.empty();
        }

        view.showMessage("Deseja ordem reversa? (S/N)");
        boolean reversed = view.getInput("").trim().equalsIgnoreCase("S");

        String criteria = switch (option) {
            case 2 -> "title";
            case 3 -> "status";
            default -> "deadline";
        };

        return Optional.of(TaskComparators.getComparator(criteria, reversed));
    }
}
