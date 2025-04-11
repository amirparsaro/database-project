package todo.entity;

import db.*;

import java.util.Date;

public class Task extends Entity implements Trackable {
    private static final int ENTITY_TASK_ID = -1;

    @Override
    public Entity copy() {
        Task copy = new Task(this.title, this.description, this.dueDate, this.status);
        copy.id = this.id;
        return copy;
    }

    @Override
    public int getEntityCode() {
        return ENTITY_TASK_ID;
    }

    @Override
    public void setCreationDate(Date date) {

    }

    @Override
    public Date getCreationDate() {
        return null;
    }

    @Override
    public void setLastModificationDate(Date date) {

    }

    @Override
    public Date getLastModificationDate() {
        return null;
    }

    public enum Status {
        NotStarted,
        InProgress,
        Completed
    }

    public String title;
    public String description;
    public Date dueDate;
    public Status status;

    public Task() {

    }

    public Task(String title, String description, Date dueDate, Status status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }
}
