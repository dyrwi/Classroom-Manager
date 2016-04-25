package com.dyrwi.classroommanager.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.model.ClassroomStudent;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.model.StudentRecord;
import com.dyrwi.classroommanager.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Ben on 30-Sep-15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String NAME = "classroommanager.db";
    private static final int VERSION = 17;
    private static final String TAG = "DatatbaseHelper";

    private Dao<Student, String> studentDao;
    private Dao<Classroom, String> classroomDao;
    private Dao<StudentRecord, String> studentRecordDao;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "onCreate()");
            TableUtils.createTable(connectionSource, Student.class);
            TableUtils.createTable(connectionSource, Classroom.class);
            TableUtils.createTable(connectionSource, StudentRecord.class);
            Log.i(TAG, "Tables created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(TAG, "onUpgrade()");
            TableUtils.dropTable(connectionSource, Student.class, true);
            TableUtils.dropTable(connectionSource, Classroom.class, true);
            TableUtils.dropTable(connectionSource, ClassroomStudent.class, true);
            TableUtils.dropTable(connectionSource, StudentRecord.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Dao<Student, String> getStudentDao() throws SQLException {
        if(studentDao == null) {
            studentDao = getDao(Student.class);
        }
        return studentDao;
    }

    public Dao<Classroom, String> getClassroomDao() throws SQLException {
        if(classroomDao == null) {
            classroomDao = getDao(Classroom.class);
        }
        return classroomDao;
    }

    public Dao<StudentRecord, String> getStudentRecordDao() throws SQLException {
        if(studentRecordDao == null)
            studentRecordDao = getDao(StudentRecord.class);
        return studentRecordDao;
    }

}
