package com.dyrwi.classroommanager.repository.initialization;

import com.dyrwi.classroommanager.model.Classroom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 06-Oct-15.
 */
public class ClassroomInitialize implements Initialize<Classroom> {
    private static final String TAG = "ClassroomInitialize";
    private ArrayList<Classroom> classrooms;

    @Override
    public void create() {
        classrooms = new ArrayList<Classroom>();
        Classroom c1 = new Classroom();
        c1.setName("Eddie, Sunny, Amy, Martin, Anna");
        classrooms.add(c1);
    }

    @Override
    public List<Classroom> getAll() {
        return classrooms;
    }
}
