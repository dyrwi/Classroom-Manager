package com.dyrwi.classroommanager.repository.initialization;

import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.model.ClassroomStudent;
import com.dyrwi.classroommanager.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 06-Oct-15.
 */
public class ClassroomStudentInitialize implements Initialize<ClassroomStudent> {
    private static final String TAG = "ClassroomStudentInitialize";
    private ArrayList<ClassroomStudent> classroomStudent;
    private ArrayList<Student> students;
    private ArrayList<Classroom> classrooms;

    public ClassroomStudentInitialize(List<Student> students, List<Classroom> classrooms) {
        this.students = (ArrayList<Student>)students;
        this.classrooms = (ArrayList<Classroom>)classrooms;
    }

    @Override
    public void create() {
        if(this.students != null && this.classrooms != null) {
        }
    }

    @Override
    public List<ClassroomStudent> getAll() {
        return null;
    }
}
