package com.epam.pablo.storage.impl;

import com.epam.pablo.entity.Entity;
import com.epam.pablo.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

@Service
@Component
public class StorageImpl<T extends Entity> implements Storage<T> {
    private final HashMap<Class<T>, HashMap<Long, T>> storage = new HashMap<>();
    private final HashMap<Class<T>, AtomicLong> idGenerators = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(StorageImpl.class);

    @Override
    public List<T> selectAll(Class<T> objectClass) {
        HashMap<Long, T> objects = storage.getOrDefault(objectClass, new HashMap<Long, T>());

        logger.info("Selecting all objects of class: {}", objectClass.getSimpleName());

        return new ArrayList<>(objects.values());
    }

    @Override
    public T getById(Class<T> objectClass, Long id) {
        return storage.getOrDefault(objectClass, new HashMap<>()).get(id);
    }

    @Override
    public T save(T object, Class<T> objectClass) {
        if (object == null) {
            logger.error("Attempted to save a null object in class {}", objectClass.getSimpleName());
            throw new IllegalArgumentException("Cannot save with a null object");
        }

        var idGenerator = idGenerators.computeIfAbsent(objectClass, k -> new AtomicLong(1));
        var id = idGenerator.getAndIncrement();
        object.setId(id);

        var map = storage.computeIfAbsent(objectClass, k -> new HashMap<>());
        map.put(id, object);

        logger.info("Saved new object with id {} in class {}", id, objectClass.getSimpleName());

        return object;
    }

    @Override
    public T update(T object, Class<T> objectClass) {
        if (object == null) {
            logger.error("Attempted to update with a null object in class {}", objectClass.getSimpleName());
            throw new IllegalArgumentException("Cannot update with a null object");
        }

        var map = storage.get(objectClass);
        if (map == null) {
            logger.error("No entries found for class {} during update attempt", objectClass.getSimpleName());
            throw new IllegalArgumentException("No entries found for class " + objectClass.getSimpleName());
        }

        var id = object.getId();
        T updatedObject = map.computeIfPresent(id, (k, v) -> object);
        if (updatedObject == null) {
            throw new IllegalArgumentException("Object with id " + id + " not found for update");
        }

        logger.info("Updated object with id {} in class {}", id, objectClass.getSimpleName());

        return updatedObject;
    }

    @Override
    public boolean delete(Class<T> objectClass, Long id) {
        var map = storage.get(objectClass);
        if (map == null) {
            logger.debug("Attempted to delete object from unmanaged class: {}", objectClass.getSimpleName());
            return false;
        }

        boolean deleted = map.remove(id) != null;
        if (deleted) {
            logger.info("Deleted object with id {} from class {}", id, objectClass.getSimpleName());
        } else {
            logger.debug("No object found with id {} in class {}", id, objectClass.getSimpleName());
        }

        return deleted;
    }
}
