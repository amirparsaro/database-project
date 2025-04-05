package todo.entity;

import db.*;

import java.util.Date;

public class Task extends Entity implements Trackable {
    @Override
    public Entity copy() {
        return null;
    }

    @Override
    public int getEntityCode() {
        return 0;
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
}
