package todo.service;

import db.Database;
import db.exception.InvalidEntityException;
import todo.entity.Step;

public class StepService {
    public static void saveStep(int taskRef, String title) {
        Step newStep = new Step(title, Step.Status.NotStarted, taskRef);

        try {
            Database.add(newStep);
        } catch (InvalidEntityException e) {
            System.out.println("[Error] Entity is invalid. Check for any wrong input.");
        }
    }

    public static void addStep(int taskId, String title) {
        Step step = new Step(title, Step.Status.NotStarted, taskId);
        try {
            Database.add(step);
        } catch (InvalidEntityException e) {
            System.out.println("[Error] Entity is invalid. Check for any wrong input.");
        }
    }
}
