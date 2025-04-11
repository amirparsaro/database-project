package todo.validator;

import todo.entity.Step;
import db.*;
import db.exception.*;

public class StepValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Step)) {
            throw new IllegalArgumentException("Entity must be a Step instance.");
        }

        if (((Step) entity).title == null) {
            throw new InvalidEntityException("Object title cannot be null.");
        }

        try {
            Entity entityFromDatabase = Database.get(((Step) entity).taskRef);
        } catch (EntityNotFoundException e) {
            System.out.println("taskRef is wrong. Entity was not found in the database.");
        }
    }}
