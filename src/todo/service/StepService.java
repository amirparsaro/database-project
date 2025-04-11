package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

import java.util.Date;

public class StepService {
    public static void saveStep(int taskRef, String title) throws InvalidEntityException {
        Step newStep = new Step(title, Step.Status.NotStarted, taskRef);

        Database.add(newStep);

    }

    public static String updateStep(int stepId, String field, String newValue)
            throws InvalidEntityException, EntityNotFoundException, IllegalArgumentException {
        String oldValue = "";

        Entity entity = Database.get(stepId);
        if (!(entity instanceof Task)) {
            throw new InvalidEntityException("Entity is not an instance of Task.");
        }
        Step step = (Step) entity;

        if (field.equals("title")) {
            oldValue = step.title;
            step.title = newValue;
        } else {
            throw new IllegalArgumentException("Unknown field detected.");
        }

        Database.update(step);

        return oldValue;
    }
}
