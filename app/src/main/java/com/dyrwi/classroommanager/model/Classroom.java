package com.dyrwi.classroommanager.model;

import android.net.Uri;
import android.util.Log;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 05-Oct-15.
 */
@DatabaseTable(tableName = "classrooms")
public class Classroom extends BaseEntity {
    private static final String TAG = "Classroom";
    @DatabaseField
    private String name;

    @ForeignCollectionField
    private ForeignCollection<Student> students;

    @DatabaseField
    private String pictureUri;

    private String koreanNames;

    public Classroom() {
        this.name = "name";
        Uri uri = Uri.parse("android.resource://com.dyrwi.classroommanger/drawable/avatar_blank");
        setPictureUri(uri.toString());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishNamesOfStudents() {
        StringBuilder sb = new StringBuilder();
        try {
            CloseableIterator<Student> iterator = getStudents().closeableIterator();
            while (iterator.hasNext()) {
                Student s = iterator.next();
                sb.append(s.getEnglishName() + " ");
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Returned null for getEnglishNames(). Setting default to return blank");
            sb.append("");
        }
        return sb.toString();
    }

    public String getKoreanNamesOfStudents() {
        if(koreanNames == null || koreanNames.equalsIgnoreCase("")){
            Log.i(TAG, "retrieving korean names of students");
            koreanNames = checkKoreanNamesOfStudents();
        }
        return koreanNames;
    }

    private String checkKoreanNamesOfStudents() {
        StringBuilder sb = new StringBuilder();
        try {
            CloseableIterator<Student> iterator = getStudents().closeableIterator();
            while (iterator.hasNext()) {
                Student s = iterator.next();
                sb.append(s.getEnglishName() + " ");
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Returned null for getEnglishNames(). Setting default to return blank");
            sb.append("");
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        CloseableIterator<Student> iterator = getStudents().closeableIterator();
        while (iterator.hasNext()) {
            Student s = iterator.next();
            sb.append(s.getKoreanAndEnglishNames());
        }
        return sb.toString();
    }

    public List<Student> getStudentsAsList() {
        List<Student> students = new ArrayList<Student>();
        try {
            ForeignCollection<Student> classroomStudents = getStudents();
            CloseableIterator<Student> iterator = classroomStudents.closeableIterator();
            while (iterator.hasNext()) {
                Student s = iterator.next();
                students.add(s);
            }
            iterator.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public ForeignCollection<Student> getStudents() {
        if (students != null) {
            return students;
        } else {
            return null;
        }
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }
}
