package com.epam.pablo.storage;

import com.epam.pablo.entity.Entity;

import java.util.List;

public interface Storage<T extends Entity> {
    List<T> selectAll(Class<T> objectClass);

    T getById(Class<T> objectClass, Long id);

    T save(T object, Class<T> objectClass);

    T update(T object, Class<T> objectClass);

    boolean delete(Class<T> objectClass, Long id);
}
