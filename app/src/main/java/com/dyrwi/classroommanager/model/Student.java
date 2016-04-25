package com.dyrwi.classroommanager.model;

import android.net.Uri;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 01-Oct-15.
 */
@DatabaseTable(tableName = "students")
public class Student extends Person {
    private static final String CLASSROOM_ID_FIELD_NAME = "classroom_id";
    public static final String STUDENT_RECORDS = "student_records";

    @DatabaseField
    private String koreanName;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = CLASSROOM_ID_FIELD_NAME)
    private Classroom classroom;

    @ForeignCollectionField
    private ForeignCollection<StudentRecord> records;

    public Student() {

    }

    public String getKoreanName() {
        if (this.koreanName == null) {
            this.koreanName = "Korean Name";
        }
        return koreanName;
    }

    public void setKoreanName(String koreanName) {
        if (koreanName == null) {
            koreanName = "Korean Name";
        }
        this.koreanName = koreanName;
    }

    public Classroom getClassroom() {
        if (this.classroom == null) {
            this.classroom = new Classroom();
        }
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        if (classroom == null) {
            classroom = new Classroom();
        }
        this.classroom = classroom;
    }

    public String toString() {
        return new StringBuilder()
                .append(getEnglishName() + ", ")
                .append(getKoreanName() + ", ")
                .append(getClassroom().getName() + ", ")
                .append(getPhoneNumber() + ", ")
                .append(getDateOfBirth().toString() + " , ")
                .toString();
    }

    public String getKoreanAndEnglishNames() {
        return new StringBuilder().append(getEnglishName() + " ").append(getKoreanName()).toString();
    }

    public ArrayList<StudentRecord> getRecordsAsList() {
        ArrayList<StudentRecord> studentRecords = new ArrayList<StudentRecord>();
        try {
            ForeignCollection<StudentRecord> recordsForStudents = getRecords();
            CloseableIterator<StudentRecord> iterator = recordsForStudents.closeableIterator();
            while(iterator.hasNext()) {
                StudentRecord sr = iterator.next();
                studentRecords.add(sr);
            }
            iterator.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentRecords;
    }

    public ForeignCollection<StudentRecord> getRecords() {
        return records;
    }
}
