package br.com.ada.taskapi.util;

import br.com.ada.taskapi.controller.TaskController;
import br.com.ada.taskapi.model.Task;

import java.time.LocalDate;

public class SampleDataInitializer {

    public static void initializeSampleTasks(TaskController controller) {
        controller.createTask("Reunião de equipe", "Discutir metas da semana", LocalDate.now(), Task.Status.PENDENTE);

        Task taskRelatorio = controller.createTask("Entrega do relatório", "Relatório financeiro mensal", LocalDate.now().plusDays(1), Task.Status.EM_ANDAMENTO);
        controller.updateTaskStatus(taskRelatorio.getId(), Task.Status.CONCLUIDO);

        controller.createTask("Atualizar backlog", "Revisar e priorizar tarefas", LocalDate.now().plusDays(2), Task.Status.PENDENTE);
        controller.createTask("Call com cliente", "Apresentação de progresso do projeto", LocalDate.now().plusDays(3), Task.Status.EM_ANDAMENTO);
        controller.createTask("Planejamento sprint", "Definição das atividades para a próxima sprint", LocalDate.now().plusDays(4), Task.Status.PENDENTE);

        System.out.println("✅ 5 tarefas de exemplo foram adicionadas!");
    }

}