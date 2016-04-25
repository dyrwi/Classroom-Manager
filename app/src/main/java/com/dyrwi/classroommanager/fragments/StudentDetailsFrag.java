package com.dyrwi.classroommanager.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.repository.Repo;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.SimpleDateFormat;

/**
 * Created by Ben on 14-Oct-15.
 */
public class StudentDetailsFrag extends Fragment {
    public static final String ID = "id";
    private static final String TAG = "StudentDetailsFrag";
    private Student student;
    private Repo repo;

    private View view;
    private View recordsView;

    // Widgets
    private ImageView mImageView;
    private TextView mDateOfBirth;
    private TextView mClassroomName;
    private TextView mPhoneNumber;
    private TextView mClassesAttended;
    private TextView mAttendanceRate;
    private TextView mHomeworkRate;


    public static StudentDetailsFrag newInstance(long id) {
        StudentDetailsFrag fragment = new StudentDetailsFrag();
        Bundle args = new Bundle();
        args.putLong(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        this.repo = new Repo(getActivity());
        this.student = repo.students.findByID(getArguments().getLong(ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {
        view = inflater.inflate(R.layout.fragment_student_details, container, false);

        ImageView mDateOfBirthIcon = (ImageView)view.findViewById(R.id.date_of_birth_icon);
        mDateOfBirthIcon.setImageDrawable(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_birthday_cake).colorRes(R.color.light_blue).sizeDp(24));
        mDateOfBirth = (TextView) view.findViewById(R.id.date_of_birth_data);
        mDateOfBirth.setText(new SimpleDateFormat("d MMM yyyy").format(student.getDateOfBirth().getTime()));

        ImageView mClassroomNameIcon = (ImageView)view.findViewById(R.id.classroom_icon);
        mClassroomNameIcon.setImageDrawable(new IconicsDrawable(getActivity()).icon(GoogleMaterial.Icon.gmd_class).colorRes(R.color.light_blue).sizeDp(24));
        mClassroomName = (TextView) view.findViewById(R.id.classroom_data);
        mClassroomName.setText(student.getClassroom().getName());

        ImageView mPhoneNumberIcon = (ImageView)view.findViewById(R.id.phone_icon);
        mPhoneNumberIcon.setImageDrawable(new IconicsDrawable(getActivity()).icon(GoogleMaterial.Icon.gmd_phone).colorRes(R.color.light_blue).sizeDp(24));

        mPhoneNumber = (TextView) view.findViewById(R.id.phone_data);
        mPhoneNumber.setText(student.getPhoneNumber());

        if(student.getRecords().size() >= 1) {
            LinearLayout recordsContainer = (LinearLayout) view.findViewById(R.id.records_holder);
            recordsView = inflater.inflate(R.layout.student_record_information, recordsContainer, false);
            mClassesAttended = (TextView) recordsView.findViewById(R.id.classes_attended_data);
            mClassesAttended.setText("IT WORKS");
            mHomeworkRate = (TextView) recordsView.findViewById(R.id.homework_rate_data);
            recordsContainer.addView(recordsView);
        } else {
            LinearLayout recordsContainer = (LinearLayout) view.findViewById(R.id.records_holder);
            TextView noRecordsFound = new TextView(getActivity());
            noRecordsFound.setText("No Records Found");
            recordsContainer.addView(noRecordsFound);
        }

        return view;
    }

    private void setStaticWidgets() {
    }
}
