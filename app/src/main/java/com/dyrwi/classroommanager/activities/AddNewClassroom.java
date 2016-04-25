package com.dyrwi.classroommanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.fragments.SimpleStudentListFrag;
import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.repository.Repo;

import java.util.ArrayList;

/**
 * Created by Ben on 07-Oct-15.
 */
public class AddNewClassroom extends AppCompatActivity {
    private static final String TAG = "AddNewClassroom";
    private static final int STUDENT_CHECK = 1;
    private Repo repo;

    // Widgets
    private EditText mName;
    private TextView mStudentNames;
    private Button mChooseStudents;
    private Button mAdd;
    private Button mCancel;
    private CheckBox mUseStudentNames;

    // Java Parameters
    private String name;
    private ArrayList<Student> students;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.add_new_classroom);
        setParameters();
        setWidgets();
    }

    private void setParameters() {
        name = "";
        students = new ArrayList<Student>();
        repo = new Repo(this);
    }

    private void setWidgets() {
        mName = (EditText)findViewById(R.id.add_new_classroom_classroom_name);
        mStudentNames = (TextView)findViewById(R.id.add_new_classroom_student_list);
        mAdd = (Button)findViewById(R.id.add_new_classroom_add);
        mAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Classroom c = new Classroom();
                repo.classrooms.create(c);
                for(Student s: students) {
                    s.setClassroom(c);
                    repo.students.update(s);
                }
                repo.classrooms.update(c);
                c.setName(mName.getText().toString());
                repo.classrooms.update(c);
                finish();
            }
        });
        mCancel = (Button)findViewById(R.id.add_new_classroom_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mChooseStudents = (Button)findViewById(R.id.add_new_classroom_student_button);
        mChooseStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick() executed on mChooseStudents");
                Intent i = new Intent(AddNewClassroom.this, SimpleStudentList.class);
                startActivityForResult(i, STUDENT_CHECK);
            }
        });
        mUseStudentNames = (CheckBox)findViewById(R.id.add_new_classroom_use_student_names);
        mUseStudentNames.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true && students.size() >= 1) {
                    setClassroomName();
                } else if(isChecked == true && students.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "No students have been added to the classroom", Toast.LENGTH_SHORT).show();
                } else if (isChecked != true) {
                    mName.setText("");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == STUDENT_CHECK && data != null) {
            if(resultCode == Activity.RESULT_OK) {
                Student s = repo.students.findByID(data.getExtras().getLong(SimpleStudentListFrag.STUDENT));
                boolean listContains = false;
                for(int i = 0; i < students.size(); i++) {
                    if(students.get(i).getId() == s.getId()) {
                        Toast toast  = Toast.makeText(this, s.getKoreanAndEnglishNames() + " is already added to the student list", Toast.LENGTH_SHORT);
                        toast.show();
                        listContains = true;
                    }
                }
                if(!listContains)
                    students.add(s);
                setStudentNamesText();
            }
        }
    }

    private void setClassroomName() {
        StringBuilder sb = new StringBuilder();
        for(Student s: students) {
            sb.append(s.getEnglishName() + "(" + s.getKoreanName() + ") ");
        }
        mName.setText(sb.toString());
    }

    private void setStudentNamesText() {
        StringBuilder sb = new StringBuilder();
        for(Student s: students) {
            sb.append(s.getKoreanAndEnglishNames() + "\n");
        }
        mStudentNames.setText(sb.toString());
    }


}
