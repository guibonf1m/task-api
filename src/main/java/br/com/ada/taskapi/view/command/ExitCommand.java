package br.com.ada.taskapi.view.command;

import br.com.ada.taskapi.view.View;

public class ExitCommand implements Command {

    private final View view;

    public ExitCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        view.showMessage("Saindo... 👋");
        System.exit(0);
    }
}
