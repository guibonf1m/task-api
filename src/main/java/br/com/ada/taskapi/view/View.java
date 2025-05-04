package br.com.ada.taskapi.view;

public interface View extends AutoCloseable {
    void showMessage(String message);
    String getInput(String prompt);
    Integer getIntInput(String prompt);
}