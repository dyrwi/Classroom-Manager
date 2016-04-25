package com.dyrwi.classroommanager.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Ben on 19-Oct-15.
 */
public class StudentRecord extends BaseEntity {
    public final static String STUDENT_FIELD_NAME = "student_id";
    public final static String CLASSROOM_FIELD_NAME = "classroom_id";
    public final static String HAS_ATTENDED_FIELD_NAME = "has_attended";
    public final static String DATE_FIELD_NAME = "date";
    public final static String HAS_HOMEWORK_FIELD_NAME = "has_homework";
    public static final String MESSAGE = "message";

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = STUDENT_FIELD_NAME)
    private Student student;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = CLASSROOM_FIELD_NAME)
    private Classroom classroom;

    @DatabaseField(columnName = DATE_FIELD_NAME)
    private String date;

    @DatabaseField(columnName = HAS_ATTENDED_FIELD_NAME)
    private boolean hasAttended;

    @DatabaseField(columnName = HAS_HOMEWORK_FIELD_NAME)
    private boolean hasHomework;

    @DatabaseField(columnName = MESSAGE)
    private String message;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public boolean isHasAttended() {
        return hasAttended;
    }

    public void setHasAttended(boolean hasAttended) {
        this.hasAttended = hasAttended;
    }

    public boolean isHasHomework() {
        return hasHomework;
    }

    public void setHasHomework(boolean hasHomework) {
        this.hasHomework = hasHomework;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(getStudent() != null)
            sb.append(getStudent().getEnglishName());
        else
            sb.append("NO STUDENT");
        if(getClassroom() != null)
            sb.append(getClassroom().getName());
        else
            sb.append("NO CLASSROOM");
        if(getDate() != null)
            sb.append(getDate());
        else
            sb.append("NO DATE");
        sb.append("Homework: " + isHasHomework());
        sb.append("Attendance: " + isHasAttended());
        sb.append(getMessage());
        return sb.toString();
    }
}
