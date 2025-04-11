package todo.service;

import java.util.Date;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Task;

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

    public static void addTask(String title, String description, String due) throws InvalidEntityException {
        String[] splitDueDate = due.split("-");
        Date dueDate = new Date(Integer.parseInt(splitDueDate[0]) - 1900, Integer.parseInt(splitDueDate[1]) - 1,
                Integer.parseInt(splitDueDate[2]));

        Task task = new Task(title, description, dueDate, Task.Status.NotStarted);
        Database.add(task);
    }

    public static void updateTask(int taskId, String field, String newValue)
            throws InvalidEntityException, EntityNotFoundException, IllegalArgumentException {
        Task task = (Task) Database.get(taskId);

        if (field.equals("title")) {
            task.title = newValue;
        } else if (field.equals("description")) {
            task.description = newValue;
        } else if (field.equals("due-date")) {
            String[] splitDueDate = newValue.split("-");
            Date dueDate = new Date(Integer.parseInt(splitDueDate[0]) - 1900, Integer.parseInt(splitDueDate[1]) - 1,
                    Integer.parseInt(splitDueDate[2]));
            task.dueDate = dueDate;
        } else {
            throw new IllegalArgumentException("Unknown field detected.");
        }

        Database.update(task);
    }
}