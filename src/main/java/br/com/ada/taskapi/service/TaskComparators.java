package br.com.ada.taskapi.service;

import br.com.ada.taskapi.model.Task;

import java.util.Comparator;

public class TaskComparators {

    public static final Comparator<Task> BY_DEADLINE = Comparator.comparing(Task::getDeadline);

    public static final Comparator<Task> BY_TITLE = Comparator.comparing(Task::getTitle);

    public static final Comparator<Task> BY_STATUS = Comparator.comparing(Task::getStatus);

    public static Comparator<Task> getComparator(String criteria, boolean reversed) {
        Comparator<Task> comparator = switch (criteria.toLowerCase()) {
            case "title" -> BY_TITLE;
            case "status" -> BY_STATUS;
            default -> BY_DEADLINE;
        };
        return reversed ? comparator.reversed() : comparator;
    }
}
