package todo.entity;

import db.*;

public class Step extends Entity {
    @Override
    public Entity copy() {
        Step copy = new Step(this.title, this.status, this.taskRef);
        copy.id = this.id;
        return copy;
    }

    @Override
    public int getEntityCode() {
        return taskRef;
    }

    public enum Status {
        NotStarted,
        Completed
    }

    public String title;
    public Status status;
    public int taskRef;

    public Step(String title, Status status, int taskRef) {
        this.title = title;
        this.status = status;
        this.taskRef = taskRef;
    }
}
