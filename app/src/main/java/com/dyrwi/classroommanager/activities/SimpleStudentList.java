package com.dyrwi.classroommanager.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.fragments.SimpleStudentListFrag;

/**
 * Created by Ben on 13-Oct-15.
 */
public class SimpleStudentList extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.activity_student_list);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction  = fragmentManager.beginTransaction();
        SimpleStudentListFrag fragment = new SimpleStudentListFrag();
        fragmentTransaction.add(R.id.swipe_refresh_layout, fragment);
        fragmentTransaction.commit();
    }
}
