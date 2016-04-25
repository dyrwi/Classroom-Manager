package com.dyrwi.classroommanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.repository.Repo;

/**
 * Created by Ben on 01-Oct-15.
 */
public class ORMLiteTest extends AppCompatActivity {
    private Repo repo;
    private static String TAG = "ORMLiteTest";

    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        repo = new Repo(this);
        setContentView(R.layout.activity_main);
    }
}
