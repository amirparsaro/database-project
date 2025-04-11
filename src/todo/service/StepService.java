package todo.service;

import db.Database;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

public class StepService {
    public static void saveStep(int taskRef, String title) {
        Step newStep = new Step(title, Task.Status.NotStarted, taskRef);

        try {
            Database.add(newStep);
        } catch (InvalidEntityException e) {
            System.out.println("[Error] Entity is invalid. Check for any wrong input.");
        }
    }
}
