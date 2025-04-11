package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

import java.util.ArrayList;
import java.util.Date;

public class StepService {
    public static void setAsCompleted(int stepId) throws InvalidEntityException, EntityNotFoundException {
        Entity entity = Database.get(stepId);
        if (!(entity instanceof Step)) {
            throw new InvalidEntityException("Entity is not an instance of Step.");
        }
        Step step = (Step) entity;

        step.status = Step.Status.Completed;

        Database.update(step);

    }

    public static int saveStep(int taskRef, String title) throws InvalidEntityException, EntityNotFoundException {
        Entity entity = Database.get(taskRef);
        if (!(entity instanceof Task)) {
            throw new InvalidEntityException("Entity is not an instance of Task.");
        }

        Step newStep = new Step(title, Step.Status.NotStarted, taskRef);

        Database.add(newStep);
        return newStep.id;
    }

    public static String updateStep(int stepId, String field, String newValue)
            throws InvalidEntityException, EntityNotFoundException, IllegalArgumentException {
        String oldValue = "";

        Entity entity = Database.get(stepId);
        if (!(entity instanceof Step)) {
            throw new InvalidEntityException("Entity is not an instance of Task.");
        }
        Step step = (Step) entity;

        if (field.equals("title")) {
            oldValue = step.title;
            step.title = newValue;
        } else if (field.equals("status")) {
            oldValue = String.valueOf(step.status);
            Task task = (Task) Database.get(step.taskRef);
            if (newValue.equals("NotStarted")) {
                step.status = Step.Status.NotStarted;
            } else if (newValue.equals("Completed")) {
                step.status = Step.Status.Completed;
                Database.update(step);
                Entity taskEntity = Database.get(step.taskRef);
                if (!(taskEntity instanceof Task)) {
                    throw new InvalidEntityException("Entity is not an instance of Task.");
                }

                changeTaskState(step.id);
            } else {
                throw new IllegalArgumentException("Unknown status detected.");
            }
        } else {
            throw new IllegalArgumentException("Unknown field detected.");
        }

        Database.update(step);

        return oldValue;
    }

    private static void changeTaskState (int stepId) throws InvalidEntityException, EntityNotFoundException {
        Entity entity = Database.get(stepId);
        if (!(entity instanceof Step)) {
            throw new InvalidEntityException("Entity is not an instance of Step.");
        }
        Step step = (Step) entity;

        ArrayList<Entity> allSteps = Database.getAll(step.taskRef);
        int count = 0;
        int i = 0;
        for (Entity e : allSteps) {
            Step eStep = (Step) e;
            if (eStep.status == Step.Status.Completed) {
                count++;
            }
            i++;
        }

        Task task = (Task) Database.get(step.taskRef);
        if (i == count) {
            TaskService.setAsCompleted(step.taskRef);
        } else if (count >= 1 && task.status.equals(Task.Status.NotStarted)) {
            task.status = Task.Status.InProgress;
            Database.update(task);
        }
    }
}
