package com.dyrwi.classroommanager.repository.implementations;

import android.util.Log;

import com.dyrwi.classroommanager.model.BaseEntity;
import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.repository.DatabaseHelper;
import com.dyrwi.classroommanager.repository.RepositoryCreation;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 05-Oct-15.
 */
public class ClassroomImpl implements RepositoryCreation<Classroom> {
    private static String TAG = "ClassroomImpl";
    Dao<com.dyrwi.classroommanager.model.Classroom, String> classroomDao;

    public ClassroomImpl(DatabaseHelper db) {
        try {
            classroomDao = db.getClassroomDao();
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving dao");
            e.printStackTrace();
        }
    }

    @Override
    public int create(Classroom classroom) {
        try {
            return classroomDao.create(classroom);
        } catch (SQLException e) {
            Log.e(TAG, "Error creating");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int createOrUpdate(Classroom classroom) {
        if(findByID(classroom.getId()) != null) {
            update(classroom);
        } else {
            create(classroom);
        }
        return 0;
    }

    @Override
    public int update(Classroom classroom) {
        try {
            classroomDao.update(classroom);
        } catch (SQLException e) {
            Log.e(TAG, "Error updating");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Classroom classroom) {
        try {
            return classroomDao.delete(classroom);
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting");
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public Classroom findByID(String id) {
        try {
            return classroomDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Classroom findByID(long id) {
        try {
            QueryBuilder<Classroom, String> qb = classroomDao.queryBuilder();
            qb.where().eq("id", id);
            PreparedQuery<Classroom> pq = qb.prepare();
            return classroomDao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Classroom> findByDateCreated(Date dateCreated) {
        try {
            QueryBuilder<Classroom, String> qb = classroomDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_CREATED_FIELD_NAME, dateCreated);
            PreparedQuery<Classroom> pq = qb.prepare();
            return classroomDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to find. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Classroom> findByDateModified(Date dateModified) {
        try {
            QueryBuilder<Classroom, String> qb = classroomDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_MODIFIED_FIELD_NAME, dateModified);
            PreparedQuery<Classroom> pq = qb.prepare();
            return classroomDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to find. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        for (Classroom c : getAll()) {
            delete(c);
        }
    }

    @Override
    public List<Classroom> getAll() {
        try {
            return classroomDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving classroom list. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    public void createFromList(List<com.dyrwi.classroommanager.model.Classroom> classrooms) {
        for (com.dyrwi.classroommanager.model.Classroom c : classrooms) {
            create(c);
        }
    }


    public Classroom findByName(String name) {
        try {
            QueryBuilder<Classroom, String> qb = classroomDao.queryBuilder();
            qb.where().eq("name", name);
            PreparedQuery<Classroom> pq = qb.prepare();
            return classroomDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving classroom");
            e.printStackTrace();
        }
        return null;
    }


}
