package todo.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import db.Database;
import db.Entity;
import db.Validator;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;
import todo.validator.StepValidator;
import todo.validator.TaskValidator;

import javax.xml.crypto.Data;

public class TaskService {
    static Scanner scanner = new Scanner(System.in);
    public static void setAsCompleted(int taskId) throws InvalidEntityException, EntityNotFoundException {
        Entity entity = Database.get(taskId);
        if (!(entity instanceof Task)) {
            throw new InvalidEntityException("Entity is not an instance of Task.");
        }
        Task task = (Task) entity;

        task.status = Task.Status.Completed;

        Database.update(task);
    }

    public static int addTask(String title, String description, String due) throws InvalidEntityException {
        String[] splitDueDate = due.split("-");
        Date dueDate = new Date(Integer.parseInt(splitDueDate[0]) - 1900, Integer.parseInt(splitDueDate[1]) - 1,
                Integer.parseInt(splitDueDate[2]));

        Task task = new Task(title, description, dueDate, Task.Status.NotStarted);
        Database.add(task);

        Database.registerValidator(task.id, new StepValidator());
        return task.id;
    }

    public static String updateTask(int taskId, String field, String newValue)
            throws InvalidEntityException, EntityNotFoundException, IllegalArgumentException {
        String oldValue = "";

        Entity entity = Database.get(taskId);
        if (!(entity instanceof Task)) {
            throw new InvalidEntityException("Entity is not an instance of Task.");
        }
        Task task = (Task) entity;

        if (field.equals("title")) {
            oldValue = task.title;
            task.title = newValue;
        } else if (field.equals("description")) {
            oldValue = task.description;
            task.description = newValue;
        } else if (field.equals("due-date")) {
            oldValue = String.valueOf(task.dueDate);
            String[] splitDueDate = newValue.split("-");
            Date dueDate = new Date(Integer.parseInt(splitDueDate[0]) - 1900, Integer.parseInt(splitDueDate[1]) - 1,
                    Integer.parseInt(splitDueDate[2]));
            task.dueDate = dueDate;
        } else if (field.equals("status")) {
            oldValue = String.valueOf(task.status);
            if (newValue.equals("NotStarted")) {
                task.status = Task.Status.NotStarted;
            } else if (newValue.equals("InProgress")) {
                task.status = Task.Status.InProgress;
            } else if (newValue.equals("Completed")) {
                task.status = Task.Status.Completed;

                setStepsAsComplete(task.id);
            } else {
                throw new IllegalArgumentException("Unknown status detected.");
            }
        } else {
            throw new IllegalArgumentException("Unknown field detected.");
        }

        Database.update(task);

        return oldValue;
    }

    private static void setStepsAsComplete(int taskId) throws InvalidEntityException, EntityNotFoundException {
        ArrayList<Entity> allSteps = Database.getAll(taskId);
        for (Entity entity : allSteps) {
            Step step = (Step) entity;
            StepService.setAsCompleted(step.id);
        }
    }

    public static void getTaskById(int taskId) throws InvalidEntityException, EntityNotFoundException {
        Entity entity = Database.get(taskId);
        if (!(entity instanceof Task)) {
            throw new InvalidEntityException("Entity is not an instance of Task.");
        }
        Task task = (Task) entity;

        ArrayList<Entity> taskSteps = Database.getAll(task.id);

        System.out.println("ID: " + task.id + "\n" +
                "Title: " + task.title + "\n" +
                "Description: " + task.description + "\n" +
                "Due Date: " + task.dueDate + "\n" +
                "Status: " + task.status + "\n" +
                "Steps:");

        boolean hasEntityToShow = false;
        for (Entity e : taskSteps) {
            Step step;
            if (e instanceof Step) {
                step = (Step) e;
                System.out.println("    + " + step.title + ":\n" +
                        "        ID: " + step.id + "\n" +
                        "        Status: " + step.status);
                hasEntityToShow = true;
            }
        }

        if (!hasEntityToShow) {
            System.out.println("    No steps for this task. Create some by using \"add step\".");
        }
    }

    public static void getAllTasks() throws InvalidEntityException, EntityNotFoundException {
        Task taskUsedForId = new Task();
        ArrayList<Entity> allTasks = Database.getAll(taskUsedForId.getEntityCode());

        if (allTasks.isEmpty()) {
            System.out.println("No tasks to show in here! Create some using \"add task\".");
            return;
        }
        for (Entity entity : allTasks) {
            if (!(entity instanceof Task)) {
                throw new InvalidEntityException("Entity is not an instance of Task.");
            }
            Task task = (Task) entity;
            getTaskById(task.id);
        }
    }

    public static void getIncompleteTasks() throws InvalidEntityException, EntityNotFoundException {
        Task taskUsedForId = new Task();
        ArrayList<Entity> allTasks = Database.getAll(taskUsedForId.getEntityCode());

        boolean hasEntityToShow = false;
        for (Entity entity : allTasks) {
            if (!(entity instanceof Task)) {
                throw new InvalidEntityException("Entity is not an instance of Task.");
            }
            Task task = (Task) entity;
            if (task.status.equals(Task.Status.NotStarted) || task.status.equals(Task.Status.InProgress)) {
                getTaskById(task.id);
                hasEntityToShow = true;
            }
        }

        if (!hasEntityToShow) {
            System.out.println("No incomplete tasks to show. You are good to go!");
        }
    }

    public static void checkForTaskDeletion(int entityId) throws EntityNotFoundException {
        Task taskForEntityCode = new Task();
        ArrayList<Entity> allTasks = Database.getAll(taskForEntityCode.getEntityCode());

        for (Entity e : allTasks) {
            if (e instanceof Task) {
                Task task = (Task) e;
                if (task.id == entityId) {
                    ArrayList<Entity> allSteps = Database.getAll(task.id);
                    for (Entity entity : allSteps) {
                        Database.delete(entity.id);
                    }
                }
            }
        }
    }

    public static void addTask() {
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
    }

    public static void delete() {
        System.out.print("ID: ");
        int entityId = Integer.parseInt(scanner.nextLine());

        try {
            TaskService.checkForTaskDeletion(entityId);
            Database.delete(entityId);
            System.out.println("Entity with ID = " + entityId + " successfully deleted.");
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot delete entity with ID = " + entityId + ".\n" +
                    "[Error] " + e.getMessage());
        }
    }

    public static void updateTask() {
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
                    "New Value: " + newValue + "\n" +
                    "Modification Date: " + now);
        } catch (InvalidEntityException | EntityNotFoundException | IllegalArgumentException e) {
            System.out.println("Cannot update task with ID = " + taskId);
            System.out.println("[Error] " + e.getMessage());
        }
    }

    public static void getTask() {
        System.out.print("ID: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        try {
            TaskService.getTaskById(taskId);
        } catch (InvalidEntityException e) {
            System.out.println("Cannot find task with ID = " + taskId);
        }
    }

    public static void getAll() {
        try {
            TaskService.getAllTasks();
        } catch (InvalidEntityException e) {
            System.out.println("There was a problem with getting tasks. Try again.");
        }
    }

    public static void getIncomplete() {
        try {
            TaskService.getIncompleteTasks();
        } catch (InvalidEntityException e) {
            System.out.println("There was a problem with getting tasks. Try again.");
        }
    }

    static {
        Task taskForEntityCode = new Task();
        Database.registerValidator(taskForEntityCode.getEntityCode(), new TaskValidator());
    }
}