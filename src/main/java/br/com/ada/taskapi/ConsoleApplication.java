package br.com.ada.taskapi;

import br.com.ada.taskapi.controller.TaskController;
import br.com.ada.taskapi.controller.TaskControllerImpl;
import br.com.ada.taskapi.repository.TaskRepositoryJsonFileImpl;
import br.com.ada.taskapi.service.TaskServiceImpl;
import br.com.ada.taskapi.view.ConsoleApp;
import br.com.ada.taskapi.view.ConsoleView;
import br.com.ada.taskapi.view.View;

public class ConsoleApplication {
    public static void main(String[] args) {
        try (View view = new ConsoleView()) {
            var taskRepository = TaskRepositoryJsonFileImpl.getInstance();
            var taskServiceImpl = new TaskServiceImpl(taskRepository); // criação única
            var taskController = new TaskControllerImpl(taskServiceImpl, taskServiceImpl); // passa duas vezes

            ConsoleApp app = new ConsoleApp(view, taskController);
            app.run();
        } catch (Exception e) {
            System.err.println("Erro durante a execução: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
