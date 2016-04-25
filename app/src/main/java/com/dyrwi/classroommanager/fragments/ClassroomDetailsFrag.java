package com.dyrwi.classroommanager.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.repository.Repo;

/**
 * Created by Ben on 16-Oct-15.
 */
public class ClassroomDetailsFrag extends Fragment{
    public static final String ID = "ID";
    private Classroom classroom;
    private Repo repo;

    public static ClassroomDetailsFrag newInstance(long id) {
        ClassroomDetailsFrag fragment = new ClassroomDetailsFrag();
        Bundle args = new Bundle();
        args.putLong(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        this.repo = new Repo(getActivity());
        this.classroom = (Classroom)repo.classrooms.findByID(getArguments().getLong(ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        TextView textView = (TextView) view.findViewById(R.id.name);
        textView.setText(classroom.getName());
        return view;
    }
}
