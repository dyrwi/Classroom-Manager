package com.dyrwi.classroommanager.repository.initialization;

import com.dyrwi.classroommanager.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 01-Oct-15.
 */
public class StudentInitialize implements Initialize {
    private static String TAG = "StudentInitialize";
    private ArrayList<Student> students;

    public StudentInitialize() {
    }

    @Override
    public void create() {
        students = new ArrayList<Student>();
        Student eddie = new Student();
        eddie.setEnglishName("Eddie");
        students.add(eddie);

        Student amy = new Student();
        amy.setEnglishName("Amy");
        students.add(amy);

        Student sunny = new Student();
        sunny.setEnglishName("Sunny");
        students.add(sunny);

        Student martin = new Student();
        martin.setEnglishName("Martin");
        students.add(martin);

        Student anna = new Student();
        anna.setEnglishName("Anna");
        students.add(anna);
    }

    @Override
    public List<Student> getAll() {
        return students;
    }
}
