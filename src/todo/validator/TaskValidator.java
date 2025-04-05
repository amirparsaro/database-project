package todo.validator;

import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;
import todo.entity.Task;

public class TaskValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Task)) {
            throw new IllegalArgumentException("Entity must be a task instance.");
        }

        if (((Task) entity).title == null) {
            throw new InvalidEntityException("Object title cannot be null.");
        }
    }
}
