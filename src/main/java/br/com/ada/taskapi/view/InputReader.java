package br.com.ada.taskapi.view;

import java.util.Scanner;

public class InputReader {
    private static final Scanner scanner = new Scanner(System.in);

    public static String readLine() {
        return scanner.nextLine();
    }

    public static int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Digite um número:");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public static void close() {
        scanner.close();
    }
}
