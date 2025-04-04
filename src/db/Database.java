package db;

import db.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    public static void add(Entity e) {
        e.id = entities.size() + 1;
        entities.add(e.copy());
    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity e : entities) {
            if (e.id == id) {
                return e.copy();
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
                entities.set(index, e.copy());
                entityFound = true;
            }
        }

        System.out.println(entityFound);
        if (!entityFound) {
            throw new EntityNotFoundException(e.id);
        }
    }

    public static void registerValidator(int entityCode, Validator validator) {
        for (int eCode : validators.keySet()) {
            if (entityCode == eCode) {
                throw new IllegalArgumentException("Validator already exists in database.");
            }
        }

        validators.put(entityCode, validator);
    }
}
