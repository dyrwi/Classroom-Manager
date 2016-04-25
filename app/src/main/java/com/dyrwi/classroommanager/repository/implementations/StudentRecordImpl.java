package com.dyrwi.classroommanager.repository.implementations;

import android.util.Log;
import com.dyrwi.classroommanager.model.BaseEntity;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.model.StudentRecord;
import com.dyrwi.classroommanager.repository.DatabaseHelper;
import com.dyrwi.classroommanager.repository.RepositoryCreation;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 19-Oct-15.
 */
public class StudentRecordImpl implements RepositoryCreation<StudentRecord>{
    private static String TAG = "StudentRecordImpl";
    Dao<StudentRecord, String> studentRecordDao;

    public StudentRecordImpl(DatabaseHelper db) {
        try {
            studentRecordDao = db.getStudentRecordDao();
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving dao");
            e.printStackTrace();
        }
    }

    @Override
    public int create(StudentRecord studentRecord) {
        try {
            return studentRecordDao.create(studentRecord);
        } catch (SQLException e) {
            Log.e(TAG, "Error creating student record");
            e.printStackTrace();
        }
        return 0;
    }

    public int createOrUpdate(StudentRecord studentRecord) {
        try {
            if(findByID(studentRecord.getId()) == null) {
                return studentRecordDao.create(studentRecord);
            } else {
                return studentRecordDao.update(studentRecord);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error to create or update:");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(StudentRecord studentRecord) {
        try {
            return studentRecordDao.update(studentRecord);
        } catch (SQLException e) {
            Log.e(TAG, "Error updating:");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(StudentRecord studentRecord) {
        try {
            return studentRecordDao.delete(studentRecord);
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public StudentRecord findByID(String id) {
        try {
            QueryBuilder<StudentRecord, String> qb = studentRecordDao.queryBuilder();
            qb.where().eq("id", id);
            PreparedQuery<StudentRecord> pq = qb.prepare();
            return studentRecordDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to find. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public StudentRecord findByID(long id) {
        try {
            QueryBuilder<StudentRecord, String> qb = studentRecordDao.queryBuilder();
            qb.where().eq("id", id);
            PreparedQuery<StudentRecord> pq = qb.prepare();
            return studentRecordDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to find. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<StudentRecord> findByDateCreated(Date dateCreated) {
        try {
            QueryBuilder<StudentRecord, String> qb = studentRecordDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_CREATED_FIELD_NAME, dateCreated);
            PreparedQuery<StudentRecord> pq = qb.prepare();
            return studentRecordDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to find. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<StudentRecord> findByDateModified(Date dateModified) {
        try {
            QueryBuilder<StudentRecord, String> qb = studentRecordDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_MODIFIED_FIELD_NAME, dateModified);
            PreparedQuery<StudentRecord> pq = qb.prepare();
            return studentRecordDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to find. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<StudentRecord> getAll() {
        try {
            return studentRecordDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "Error finding all. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        for(StudentRecord sr: getAll()) {
            delete(sr);
        }
    }

    public List<StudentRecord> findByDate(String date) {
        try {
            QueryBuilder<StudentRecord, String> qb = studentRecordDao.queryBuilder();
            qb.where().eq(StudentRecord.DATE_FIELD_NAME, date);
            PreparedQuery<StudentRecord> pq = qb.prepare();
            return studentRecordDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to find. Returning null");
            e.printStackTrace();
        }
        return null;
    }

    public List<StudentRecord> findByStudent(Student student) {
        try {
            QueryBuilder<StudentRecord, String> qb = studentRecordDao.queryBuilder();
            qb.where().eq(StudentRecord.STUDENT_FIELD_NAME, student);
            PreparedQuery<StudentRecord> pq =qb.prepare();
            return studentRecordDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to find. Returning null");
            e.printStackTrace();
        }
        return null;
    }
}
