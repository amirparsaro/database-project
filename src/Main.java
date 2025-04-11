import java.util.Date;
import java.util.Scanner;

import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
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
                    System.out.print("Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Description: ");
                    String description = scanner.nextLine();
                    System.out.print("Due date: ");
                    String dueDate = scanner.nextLine();

                    try {
                        int taskId = TaskService.addTask(title, description, dueDate);
                        System.out.println("Task saved successfully.\n" +
                                "ID: " + taskId);
                    } catch (InvalidEntityException e) {
                        System.out.println("Cannot save step.\n" +
                                "[Error] " + e.getMessage());
                    }

                } else if (splitInput[1].equals("step")) {
                    System.out.print("TaskID: ");
                    int taskId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Title: ");
                    String title = scanner.nextLine();

                    try {
                        int stepId = StepService.saveStep(taskId, title);
                        Date taskDate = new Date();
                        System.out.println("Step saved successfully.\n" +
                                "ID: " + stepId + "\n" +
                                "Creation Date: " + taskDate);
                    } catch (InvalidEntityException e) {
                        System.out.println("Cannot save step.\n" +
                                "[Error] " + e.getMessage());
                    }

                } else {
                    System.out.println("[Error] invalid input: \"" + splitInput[1] + "\".Try again.");
                }


            } else if (input.equals("delete")) {
                System.out.print("ID: ");
                int entityId = Integer.parseInt(scanner.nextLine());

                try {
                    Database.delete(entityId);
                    System.out.println("Entity with ID = " + entityId + " successfully deleted.");
                } catch (EntityNotFoundException e) {
                    System.out.println("Cannot delete entity with ID = " + entityId + ".\n" +
                            "[Error] " + e.getMessage());
                }


            } else if (input.startsWith("update")) {
                String[] splitInput = input.split(" ");

                if (splitInput[1].equals("task")) {
                    System.out.print("ID: ");
                    int taskId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Field (title, description, due-date, status): ");
                    String field = scanner.nextLine();
                    System.out.print("New Value (if it's status, use: NotStarted, InProgress, Completed): ");
                    String newValue = scanner.nextLine();

                    try {
                        String oldValue = TaskService.updateTask(taskId, field, newValue);
                        Date now = new Date();
                        System.out.println("Successfully updated the task.\n" +
                                "Field: " + field + "\n" +
                                "Old Value: " + oldValue + "\n" +
                                "New Value:" + newValue + "\n" +
                                "Modification Date: " + now);
                    } catch (InvalidEntityException | EntityNotFoundException | IllegalArgumentException e) {
                        System.out.println("Cannot update task with ID = " + taskId);
                        System.out.println("[Error] " + e.getMessage());
                    }

                } else if (splitInput[1].equals("step")) {
                    System.out.print("ID: ");
                    int stepId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Field (title, status): ");
                    String field = scanner.nextLine();
                    System.out.print("New Value (if it's status, use: NotStarted, Completed): ");
                    String newValue = scanner.nextLine();

                    try {
                        String oldValue = StepService.updateStep(stepId, field, newValue);
                        Date now = new Date();
                        System.out.println("Successfully updated the step.\n" +
                                "Field: " + field + "\n" +
                                "Old Value: " + oldValue + "\n" +
                                "New Value:" + newValue + "\n" +
                                "Modification Date: " + now);
                    } catch (InvalidEntityException | EntityNotFoundException | IllegalArgumentException e) {
                        System.out.println("Cannot update step with ID = " + stepId);
                        System.out.println("[Error] " + e.getMessage());
                    }
                } else {
                System.out.println("[Error] invalid input: \"" + splitInput[1] + "\".Try again.");
            }


            } else if (input.startsWith("get")) {
                String[] splitInput = input.split(" ");

                if (splitInput[1].equals("task-by-id")) {
                    System.out.print("ID: ");
                    int taskId = Integer.parseInt(scanner.nextLine());

                    try {
                        TaskService.getTaskById(taskId);
                    } catch (InvalidEntityException e) {
                        System.out.println("Cannot find task with ID = " + taskId);
                    }

                } else if (splitInput[1].equals("all-tasks")) {
                    try {
                        TaskService.getAllTasks();
                    } catch (InvalidEntityException e) {
                        System.out.println("There was a problem with getting tasks. Try again.");
                    }

                } else if (splitInput[1].equals("incomplete-tasks")) {
                    try {
                        TaskService.getAllTasks();
                    } catch (InvalidEntityException e) {
                        System.out.println("There was a problem with getting tasks. Try again.");
                    }
                } else {
                    System.out.println("[Error] invalid input: \"" + splitInput[1] + "\".Try again.");
                }
            }

            input = scanner.nextLine();
        }
    }
}