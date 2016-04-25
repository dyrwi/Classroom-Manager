package com.dyrwi.classroommanager.repository.initialization;

import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.model.Student;

import java.util.ArrayList;

/**
 * Created by Ben on 06-Oct-15.
 */
public class MasterInitialize {
    private ArrayList<Student> students;
    private ArrayList<Classroom> classrooms;

    public MasterInitialize() {
        classrooms = new ArrayList<Classroom>();
        Classroom empty = new Classroom();
        empty.setName("No Class");
        classrooms.add(empty);

        Classroom c1 = new Classroom();
        c1.setName("Eddie Sunny Amy Martin Anna");
        classrooms.add(c1);

        students = new ArrayList<Student>();
        Student amy = new Student();
        amy.setEnglishName("Amy");
        amy.setClassroom(c1);
        amy.setPictureUri("content://media/external/images/media/430943");
        students.add(amy);

        Student sunny = new Student();
        sunny.setEnglishName("Sunny");
        sunny.setClassroom(c1);
        sunny.setPictureUri("content://media/external/images/media/430944");
        students.add(sunny);

        Student martin = new Student();
        martin.setEnglishName("Martin");
        martin.setClassroom(c1);
        martin.setPictureUri("content://media/external/images/media/430945");
        students.add(martin);

        Student anna = new Student();
        anna.setEnglishName("Anna");
        anna.setClassroom(c1);
        anna.setPictureUri("content://media/external/images/media/430946");
        students.add(anna);

        Classroom c2 = new Classroom();
        c2.setName("Peter Tim Mina");
        classrooms.add(c2);

        Student mina = new Student();
        mina.setEnglishName("Mina");
        mina.setClassroom(c2);
        mina.setPictureUri("content://media/external/images/media/430947");
        students.add(mina);

        Student tim = new Student();
        tim.setEnglishName("Tim");
        tim.setClassroom(c2);
        tim.setPictureUri("content://media/external/images/media/430948");
        students.add(tim);

        Student peter = new Student();
        peter.setEnglishName("Peter");
        peter.setClassroom(c2);
        peter.setPictureUri("content://media/external/images/media/430949");
        students.add(peter);

        Classroom c3 = new Classroom();
        c3.setName("Kate Gabin Tim Jay Chris");
        classrooms.add(c3);

        Student kate = new Student();
        kate.setEnglishName("Kate");
        kate.setClassroom(c3);
        kate.setPictureUri("content://media/external/images/media/430950");
        students.add(kate);

        Student gabin = new Student();
        gabin.setEnglishName("Gabin");
        gabin.setClassroom(c3);
        gabin.setPictureUri("content://media/external/images/media/430951");
        students.add(gabin);

        Student tim2 = new Student();
        tim2.setEnglishName("Tim");
        tim2.setClassroom(c3);
        tim2.setPictureUri("content://media/external/images/media/430952");
        students.add(tim2);

        Student jay = new Student();
        jay.setEnglishName("Jay");
        jay.setClassroom(c3);
        jay.setPictureUri("content://media/external/images/media/430953");
        students.add(jay);

        Student chris = new Student();
        chris.setEnglishName("Chris");
        chris.setClassroom(c3);
        chris.setPictureUri("content://media/external/images/media/430954");
        students.add(chris);

        Classroom c4 = new Classroom();
        c4.setName("Zack John Kelly Ella");
        classrooms.add(c4);

        Student zack = new Student();
        zack.setEnglishName("Zack");
        zack.setClassroom(c4);
        zack.setPictureUri("content://media/external/images/media/430955");
        students.add(zack);

        Student kelly = new Student();
        kelly.setEnglishName("Kelly");
        kelly.setClassroom(c4);
        kelly.setPictureUri("content://media/external/images/media/430956");
        students.add(kelly);

        Student john = new Student();
        john.setEnglishName("John");
        john.setClassroom(c4);
        john.setPictureUri("content://media/external/images/media/430957");
        students.add(john);

        Student ella = new Student();
        ella.setEnglishName("Ella");
        ella.setClassroom(c4);
        ella.setPictureUri("content://media/external/images/media/430982");
        students.add(ella);
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public void setClassrooms(ArrayList<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Classroom> getClassrooms() {
        return classrooms;
    }
}
