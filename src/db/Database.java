package db;

import db.exception.EntityNotFoundException;

import java.util.ArrayList;

public class Database {
    private static ArrayList<Entity> entities;

    public static void add(Entity e) {
        entities.add(e);
        e.id = entities.size() + 1;
    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity e : entities) {
            if (e.id == id) {
                return e;
            }
        }

        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException {
        boolean entityFound = false;
        for (Entity e : entities) {
            if (e.id == id) {
                entities.remove(e);
                entityFound = true;
            }
        }

        if (!entityFound)
            throw new EntityNotFoundException(id);
    }

    public static void update(Entity e) throws EntityNotFoundException {
        boolean entityFound = false;
        for (Entity entity : entities) {
            if (entity.id == e.id) {
                int index = entities.indexOf(entity);
                entities.set(index, e);
                entityFound = true;
            }
        }

        if (!entityFound) {
            throw new EntityNotFoundException(e.id);
        }
    }
}
