package br.com.ada.taskapi.view;

public class ConsoleView implements View {

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return InputReader.readLine();
    }

    @Override
    public Integer getIntInput(String prompt) {
        System.out.print(prompt + ": ");
        return InputReader.readInt();
    }

    @Override
    public void close() {
        InputReader.close();
    }
}
