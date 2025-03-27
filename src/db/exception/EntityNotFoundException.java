package db.exception;

public class EntityNotFoundException extends Exception {
    EntityNotFoundException() {
        super("Cannot find entity.");
    }

    EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(int id) {
        super("Cannot find entity with id = " + id + ".");
    }
}
