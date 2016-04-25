package com.dyrwi.classroommanager.repository.implementations;

import android.util.Log;

import com.dyrwi.classroommanager.model.BaseEntity;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.repository.DatabaseHelper;
import com.dyrwi.classroommanager.repository.RepositoryCreation;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 01-Oct-15.
 */
public class StudentImpl implements RepositoryCreation<Student> {
    private static String TAG = "StudentImpl";
    Dao<Student, String> studentDao;

    public StudentImpl(DatabaseHelper db) {
        try {
            studentDao = db.getStudentDao();
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving dao");
            e.printStackTrace();
        }
    }

    @Override
    public int create(Student student) {
        try {
            return studentDao.create(student);
        } catch (SQLException e) {
            Log.e(TAG, "Error creating student: " + student.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int createOrUpdate(Student student) {
        if(findByID(student.getId()) != null) {
            update(student);
        } else {
            create(student);
        }
        return 0;
    }

    @Override
    public int update(Student student) {
        try {
            return studentDao.update(student);
        } catch (SQLException e) {
            Log.e(TAG, "Error updating student: " + student.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Student student) {
        try {
            return studentDao.delete(student);
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting student: " + student.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Student findByID(String id) {
        try {
            QueryBuilder<Student, String> qb = studentDao.queryBuilder();
            qb.where().eq("id", id);
            PreparedQuery<Student> pq = qb.prepare();
            return studentDao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Unable to find student with id: " );
        return null;
    }

    @Override
    public Student findByID(long id) {
        try {
            QueryBuilder<Student, String> qb = studentDao.queryBuilder();
            qb.where().eq("id", id);
            PreparedQuery<Student> pq = qb.prepare();
            return studentDao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Unable to find student with id: " );
        return null;
    }

    @Override
    public List<Student> findByDateCreated(Date dateCreated) {
        try {
            QueryBuilder<Student, String> qb = studentDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_CREATED_FIELD_NAME, dateCreated);
            PreparedQuery<Student> pq = qb.prepare();
            return studentDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to find. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> findByDateModified(Date dateModified) {
        try {
            QueryBuilder<Student, String> qb = studentDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_MODIFIED_FIELD_NAME, dateModified);
            PreparedQuery<Student> pq = qb.prepare();
            return studentDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to find. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> findByEnglishName(String englishName) {
        try {
            QueryBuilder<Student, String> qb = studentDao.queryBuilder();
            qb.where().eq("username", englishName);
            PreparedQuery<Student> pq = qb.prepare();
            return studentDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error finding student with name: " + englishName);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> getAll() {
        try {
            return studentDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving all students");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        for (Student s : getAll()) {
            delete(s);
        }
    }

    public void createFromList(List<Student> students) {
        for (Student s : students) {
            create(s);
        }
    }
}
