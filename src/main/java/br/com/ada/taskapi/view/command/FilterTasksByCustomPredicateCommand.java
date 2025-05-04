package br.com.ada.taskapi.view.command;

import br.com.ada.taskapi.controller.TaskController;
import br.com.ada.taskapi.model.Task;
import br.com.ada.taskapi.view.View;
import br.com.ada.taskapi.service.TaskComparators;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FilterTasksByCustomPredicateCommand implements Command {

    private final View view;
    private final TaskController taskController;

    public FilterTasksByCustomPredicateCommand(View view, TaskController taskController) {
        this.view = view;
        this.taskController = taskController;
    }

    @Override
    public void execute() {
        String keyword = view.getInput("🔎 Digite uma palavra-chave para buscar no título ou descrição");
        Optional<Comparator<Task>> orderBy = getSortingMethod();

        Predicate<Task> predicate = task -> task.getTitle().contains(keyword) || task.getDescription().contains(keyword);
        List<Task> tasks = taskController.getTasksBy(predicate, orderBy);

        if (tasks.isEmpty()) {
            view.showMessage("📭 Nenhuma tarefa encontrada.");
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
