package todo.service;

import java.util.ArrayList;
import java.util.Date;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

import javax.xml.crypto.Data;

public class TaskService {
    public static void setAsCompleted(int taskId) throws InvalidEntityException { ////
        Entity entity = Database.get(taskId);
        if (!(entity instanceof Task)) {
            throw new InvalidEntityException("Entity is not an instance of Task.");
        }
        Task task = (Task) entity;

        task.status = Task.Status.Completed;

        Database.update(task);
    }

    public static void addTask(String title, String description, String due) throws InvalidEntityException {
        String[] splitDueDate = due.split("-");
        Date dueDate = new Date(Integer.parseInt(splitDueDate[0]) - 1900, Integer.parseInt(splitDueDate[1]) - 1,
                Integer.parseInt(splitDueDate[2]));

        Task task = new Task(title, description, dueDate, Task.Status.NotStarted);
        Database.add(task);
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
}