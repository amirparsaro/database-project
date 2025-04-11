package todo.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

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
    public static void setAsCompleted(int taskId) throws InvalidEntityException {
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

    private static void setStepsAsComplete(int taskId) throws InvalidEntityException {
        ArrayList<Entity> allSteps = Database.getAll(taskId);
        for (Entity entity : allSteps) {
            Step step = (Step) entity;
            StepService.setAsCompleted(step.id);
        }
    }

    public static void getTaskById(int taskId) throws InvalidEntityException {
        Entity entity = Database.get(taskId);
        if (!(entity instanceof Task)) {
            throw new InvalidEntityException("Entity is not an instance of Task.");
        }
        Task task = (Task) entity;

        ArrayList<Entity> taskSteps = Database.getAll(task.id);

        System.out.println("ID: " + task.id + "\n" +
                "Title: " + task.title + "\n" +
                "Due Date: " + task.dueDate + "\n" +
                "Status: " + task.status + "\n" +
                "Steps:");

        for (Entity e : taskSteps) {
            Step step;
            if (e instanceof Step) {
                step = (Step) e;
                System.out.println("    + " + step.title + ":\n" +
                        "        ID: " + step.id + "\n" +
                        "        Status: " + step.status);
            }
        }
    }

    public static void getAllTasks() throws InvalidEntityException {
        Task taskUsedForId = new Task();
        ArrayList<Entity> allTasks = Database.getAll(taskUsedForId.getEntityCode());

        for (Entity entity : allTasks) {
            if (!(entity instanceof Task)) {
                throw new InvalidEntityException("Entity is not an instance of Task.");
            }
            Task task = (Task) entity;
            getTaskById(task.id);
        }
    }

    public static void getIncompleteTasks() throws InvalidEntityException {
        Task taskUsedForId = new Task();
        ArrayList<Entity> allTasks = Database.getAll(taskUsedForId.getEntityCode());

        for (Entity entity : allTasks) {
            int amount = 0;
            if (!(entity instanceof Task)) {
                throw new InvalidEntityException("Entity is not an instance of Task.");
            }
            Task task = (Task) entity;
            if (task.status.equals(Task.Status.NotStarted) || task.status.equals(Task.Status.InProgress)) {
                getTaskById(task.id);
                amount++;
            }

            if (amount == 0) {
                System.out.println("No incomplete tasks to show. You are good to go!");
            }
        }
    }

    static {
        Task taskForEntityCode = new Task();
        Database.registerValidator(taskForEntityCode.getEntityCode(), new TaskValidator());
    }
}