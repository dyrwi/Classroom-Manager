package com.dyrwi.classroommanager.fragments;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.model.StudentRecord;
import com.dyrwi.classroommanager.repository.Repo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Ben on 25-Oct-15.
 */
public class StudentRecordList extends ListFragment {
    // Static
    private static final String TAG = "StudentRecordList";
    public static final String STUDENT_ID = "student_id";

    // Java
    private Repo repo;
    private ArrayList<StudentRecord> studentRecords;
    private Student student;

    // Widgets
    private ListAdapter adapter;

    public static StudentRecordList newInstance(long id) {
        StudentRecordList frag = new StudentRecordList();
        Bundle args = new Bundle();
        args.putLong(STUDENT_ID, id);
        Log.i(TAG, "Supplied ID is: " + id);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        repo = new Repo(getActivity());
        student = repo.students.findByID(getArguments().getLong(STUDENT_ID));
        studentRecords = (ArrayList<StudentRecord>) repo.studentRecords.findByStudent(student);
        Collections.sort(studentRecords, new SortByDate());
        if(studentRecords.size() >= 1)
            adapter = new StudentRecordListAdapter(getActivity(), studentRecords);
        else {
            ArrayList<StudentRecord> emptySR = new ArrayList<StudentRecord>();
            emptySR.add(new StudentRecord());
            adapter = new StudentRecordListAdapterEmpty(getActivity(), emptySR);
        }

        setListAdapter(adapter);
    }

    public void onListItemClick(ListView listView, View view, int position, long id) {
        Log.i(TAG, "HIT");
    }


    private class StudentRecordListAdapter extends ArrayAdapter<StudentRecord> {
        private Context context;

        public StudentRecordListAdapter(Context context, ArrayList<StudentRecord> studentRecords) {
            super(context, android.R.layout.simple_list_item_1, studentRecords);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.fragment_student_record_show, parent, false);
            StudentRecord sr = getItem(position);
            String dateToShow;
            TextView mDate = (TextView) rowView.findViewById(R.id.fragment_student_record_show_date);
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = formatter.parse(sr.getDate());
                dateToShow = new SimpleDateFormat("E, MMM dd yyyy").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                dateToShow = sr.getDate();
            }
            mDate.setText(dateToShow);

            CheckBox mAttendance = (CheckBox) rowView.findViewById(R.id.fragment_student_record_show_attendance);
            mAttendance.setChecked(sr.isHasAttended());

            CheckBox mHomework = (CheckBox) rowView.findViewById(R.id.fragment_student_record_show_homework);
            mHomework.setChecked(sr.isHasHomework());

            if (sr.getMessage() != null && sr.getMessage().length() >= 1) {
                Button messageBox = new Button(getActivity());
                LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.notes_layout);
                layout.addView(messageBox);
                Log.i(TAG, "message is " + sr.getMessage());
            }

            return rowView;
        }
    }

    private class SortByDate implements java.util.Comparator<StudentRecord> {
        @Override
        public int compare(StudentRecord sr1, StudentRecord sr2) {
            Date date1 = new Date();
            Date date2 = new Date();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date1 = formatter.parse(sr1.getDate());
                date2 = formatter.parse(sr2.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date2.compareTo(date1);
        }
    }

    private class StudentRecordListAdapterEmpty extends ArrayAdapter<StudentRecord> {
        private Context context;

        public StudentRecordListAdapterEmpty(Context context, ArrayList<StudentRecord> studentRecords) {
            super(context, android.R.layout.simple_list_item_1, studentRecords);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.empty_list, parent, false);
            TextView mEmpty = (TextView)rowView.findViewById(R.id.empty_list);
            mEmpty.setText("The student has no records to show");
            return rowView;
        }
    }
}
