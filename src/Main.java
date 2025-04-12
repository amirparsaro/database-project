import java.util.Scanner;

import todo.service.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to to-do list!");
        System.out.println("Use help for a list of valid commands.");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!input.equals("exit")) {
            if (input.equals("help")) {
                System.out.println("Help List\n" +
                        "\"add task/step\": Add a certain task or step to the database.\n" +
                        "\"delete\": delete a certain entity from the database.\n" +
                        "\"update task/step\": update a certain task or step from the database.\n" +
                        "\"get task-by-id/all-tasks/incomplete-tasks\": get the description for a certain task with given argument.\n" +
                        "\"help\": print this list of commands\n" +
                        "\"exit\": exit the program.\n");

            } else if (input.startsWith("add")) {
                String[] splitInput = input.split(" ");
                if (splitInput[1].equals("task")) {
                    TaskService.addTask();
                } else if (splitInput[1].equals("step")) {
                    StepService.addStep();
                } else {
                    System.out.println("[Error] invalid input: \"" + splitInput[1] + "\".Try again.");
                }

            } else if (input.equals("delete")) {
                TaskService.delete();

            } else if (input.startsWith("update")) {
                String[] splitInput = input.split(" ");
                if (splitInput[1].equals("task")) {
                    TaskService.updateTask();
                } else if (splitInput[1].equals("step")) {
                    StepService.updateStep();
                } else {
                    System.out.println("[Error] invalid input: \"" + splitInput[1] + "\".Try again.");
                }

            } else if (input.startsWith("get")) {
                String[] splitInput = input.split(" ");

                if (splitInput[1].equals("task-by-id")) {
                    TaskService.getTask();
                } else if (splitInput[1].equals("all-tasks")) {
                    TaskService.getAll();
                } else if (splitInput[1].equals("incomplete-tasks")) {
                    TaskService.getIncomplete();
                } else {
                    System.out.println("[Error] invalid input: \"" + splitInput[1] + "\".Try again.");
                }

            } else {
                System.out.println("[Error] Invalid input. Use \"help\" if you are stuck.");
            }
            input = scanner.nextLine();
        }
    }
}