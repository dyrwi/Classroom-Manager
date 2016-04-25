package com.dyrwi.classroommanager.util;

import com.dyrwi.classroommanager.model.Student;

/**
 * Created by Ben on 14-Oct-15.
 */
public class StudentSortByName implements java.util.Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return s1.getEnglishName().toLowerCase().compareToIgnoreCase(s2.getEnglishName());
    }
}
