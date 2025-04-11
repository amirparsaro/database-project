package todo.service;

import java.util.Date;
import db.Database;
import db.Entity;
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

        try {
            Database.update(task);
        } catch (InvalidEntityException e) {
            System.out.println("[Error] Entity is invalid. Check for any wrong input.");
        }
    }

    public static void addTask(String title, String description, String due) {
        String[] splitDueDate = due.split("-");
        Date dueDate = new Date(Integer.parseInt(splitDueDate[0]), Integer.parseInt(splitDueDate[1]),
                Integer.parseInt(splitDueDate[2]));

        Task task = new Task(title, description, dueDate);
        try {
            Database.add(task);
        } catch (InvalidEntityException e) {
            System.out.println("[Error] Entity is invalid. Check for any wrong input.");
        }
    }
}