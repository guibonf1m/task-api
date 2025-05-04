package br.com.ada.taskapi.view;

import br.com.ada.taskapi.model.Task;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StatusViewHelper {

    public static String getTaskAvailableStatus() {
        return Arrays.stream(Task.Status.values())
                .map(Task.Status::getDescricao)
                .collect(Collectors.joining(", "));
    }

}
