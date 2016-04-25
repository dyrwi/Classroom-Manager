package com.dyrwi.classroommanager.util;

import com.dyrwi.classroommanager.model.Classroom;

import java.util.List;

/**
 * Created by Ben on 29-Oct-15.
 */
public class ClassroomSort {

    // Set to private so this class cannot be initialized.
    private ClassroomSort() {
    }

    public static class byName implements java.util.Comparator<Classroom> {
        @Override
        public int compare(Classroom o1, Classroom o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    public static class byNumberOfStudents implements java.util.Comparator<Classroom> {
        @Override
        public int compare(Classroom o1, Classroom o2) {
            return o1.getStudentsAsList().size() - o2.getStudentsAsList().size();
        }
    }
}