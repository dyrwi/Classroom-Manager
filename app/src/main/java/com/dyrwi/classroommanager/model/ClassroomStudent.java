package com.dyrwi.classroommanager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ben on 05-Oct-15.
 */
@DatabaseTable
public class ClassroomStudent extends BaseEntity {
    public final static String STUDENT_ID_FIELD_NAME = "student_id";
    public final static String CLASSROOM_ID_FIELD_NAME = "classroom_id";

    @DatabaseField(foreign = true, columnName = STUDENT_ID_FIELD_NAME)
    private Student student;

    @DatabaseField(foreign = true, columnName = CLASSROOM_ID_FIELD_NAME)
    private Classroom classroom;

    public ClassroomStudent() {

    }

    public ClassroomStudent(Classroom classroom, Student student) {
        this.classroom = classroom;
        this.student = student;
    }

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

    public String toString() {
        return this.getClassroom().getName();
    }
}
