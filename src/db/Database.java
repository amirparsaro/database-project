package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    public static void add(Entity e) throws InvalidEntityException {
        if (e instanceof Trackable) {
            Date today = new Date();
            ((Trackable) e).setCreationDate(today);
        }

        Validator validator = validators.get(e.getEntityCode());
        validator.validate(e);


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

    public static void update(Entity e) throws EntityNotFoundException, InvalidEntityException {
        if (e instanceof Trackable) {
            Date today = new Date();
            ((Trackable) e).setLastModificationDate(today);
        }

        Validator validator = validators.get(e.getEntityCode());
        validator.validate(e);

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

    public static ArrayList<Entity> getAll(int entityCode) throws EntityNotFoundException {
        ArrayList<Entity> returnedEntities = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getEntityCode() == entityCode) {
                returnedEntities.add(entity);
            }
        }

        if (returnedEntities == null) {
            throw new EntityNotFoundException("No entities found with the given code.");
        }
        return returnedEntities;
    }
}
