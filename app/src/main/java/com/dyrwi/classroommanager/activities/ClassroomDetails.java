package com.dyrwi.classroommanager.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.fragments.ClassroomDetailsFrag;
import com.dyrwi.classroommanager.fragments.StudentDetailsFrag;

/**
 * Created by Ben on 16-Oct-15.
 */
public class ClassroomDetails extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.fragment_blank);
        long id = getIntent().getLongExtra(ClassroomDetailsFrag.ID, 0);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.content_frame, ClassroomDetailsFrag.newInstance(id)).commit();
    }
}
