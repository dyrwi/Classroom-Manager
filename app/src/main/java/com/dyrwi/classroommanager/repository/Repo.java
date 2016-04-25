package com.dyrwi.classroommanager.repository;

import android.content.Context;

import com.dyrwi.classroommanager.repository.implementations.ClassroomImpl;
import com.dyrwi.classroommanager.repository.implementations.StudentImpl;
import com.dyrwi.classroommanager.repository.implementations.StudentRecordImpl;
import com.dyrwi.classroommanager.repository.initialization.MasterInitialize;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Ben on 01-Oct-15.
 */
public class Repo {
    private static final String TAG = "Repo";
    DatabaseHelper db;
    public StudentImpl students;
    public ClassroomImpl classrooms;
    public StudentRecordImpl studentRecords;

    public Repo(Context context) {
        db = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        students = new StudentImpl(db);
        classrooms = new ClassroomImpl(db);
        studentRecords = new StudentRecordImpl(db);
    }

    public void deleteAllData() {
        students.deleteAll();
        classrooms.deleteAll();
        studentRecords.deleteAll();
    }


    private void insertAllData() {
        MasterInitialize mi = new MasterInitialize();
        classrooms.createFromList(mi.getClassrooms());
        students.createFromList(mi.getStudents());
    }
    /*
        My factoryReset function that will clear all the databases of data, insert pre-made data
        based off the classes in the repository/initialization folder. This is just a function
        that will help me reset my application without rebuilding the applications database all the
        time.
     */
    public void factoryReset() {
        deleteAllData();
        insertAllData();
    }

    public void deleteStudentRecords() {
        studentRecords.deleteAll();
    }
}
