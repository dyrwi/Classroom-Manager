package com.dyrwi.classroommanager.repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 19-Oct-15.
 */
public interface RepositoryCreation<T> {
    int create(T t);
    int createOrUpdate(T t);
    int update(T t);
    int delete(T t);
    T findByID(String id);
    T findByID(long id);
    List<T> findByDateCreated(Date dateCreated);
    List<T> findByDateModified(Date dateModified);
    List<T> getAll();
    void deleteAll();
}
