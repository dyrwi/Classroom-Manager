package com.dyrwi.classroommanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.activities.AddNewClassroom;
import com.dyrwi.classroommanager.activities.AddEditStudent;
import com.dyrwi.classroommanager.activities.ClassroomRecordActivity;
import com.dyrwi.classroommanager.activities.NewToolbarMainActivity;

/**
 * Created by Ben on 14-Oct-15.
 */
public class FeedFrag extends Fragment implements View.OnClickListener {
    private LinearLayout addRecord;
    private LinearLayout addStudent;
    private LinearLayout addClassroom;
    private LinearLayout settings;
    private LinearLayout help;

    public static FeedFrag newInstance() {
        FeedFrag fragment = new FeedFrag();
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        addRecord = (LinearLayout)view.findViewById(R.id.add_record);
        addRecord.setOnClickListener(this);
        addStudent = (LinearLayout)view.findViewById(R.id.add_student);
        addStudent.setOnClickListener(this);
        addClassroom = (LinearLayout)view.findViewById(R.id.add_classroom);
        addClassroom.setOnClickListener(this);
        settings = (LinearLayout)view.findViewById(R.id.settings);
        settings.setOnClickListener(this);
        help = (LinearLayout)view.findViewById(R.id.help);
        help.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case(R.id.add_record):
                Intent classroomRecordIntent = new Intent(getActivity().getApplicationContext(), ClassroomRecordActivity.class);
                startActivity(classroomRecordIntent);
                break;
            case(R.id.add_student):
                Intent addStudentIntent = new Intent(getActivity().getApplicationContext(), AddEditStudent.class);
                startActivityForResult(addStudentIntent, NewToolbarMainActivity.UPDATE_STUDENT_LIST);
                break;
            case(R.id.add_classroom):
                Intent classroomIntent = new Intent(getActivity().getApplicationContext(), AddNewClassroom.class);
                startActivity(classroomIntent);
                break;
            case(R.id.settings):
                Snackbar.make(getView(), "Settings", Snackbar.LENGTH_SHORT).show();
                break;
            case(R.id.help):
                Snackbar.make(getView(), "Help", Snackbar.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
