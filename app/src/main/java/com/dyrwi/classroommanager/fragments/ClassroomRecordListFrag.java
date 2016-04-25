package com.dyrwi.classroommanager.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.model.StudentRecord;
import com.dyrwi.classroommanager.repository.Repo;
import com.dyrwi.classroommanager.util.StudentSortByName;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ben on 21-Oct-15.
 */
public class ClassroomRecordListFrag extends ListFragment {
    // Static
    private static final String TAG = "ClassroomRecordListFrag";
    public static final String CLASSROOM_ID = "classroom";
    public static final String DATE = "date";

    // Java
    private Repo repo;
    private Classroom classroom;
    private ArrayList<Student> studentList;
    private ArrayList<StudentRecord> studentRecords;
    private String date;

    // Widgets
    private ListAdapter adapter;

    public static ClassroomRecordListFrag newInstance(long id, String date) {
        ClassroomRecordListFrag fragment = new ClassroomRecordListFrag();
        Bundle args = new Bundle();
        args.putLong(CLASSROOM_ID, id);
        args.putString(DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        repo = new Repo(getActivity());
        this.classroom = repo.classrooms.findByID(getArguments().getLong(CLASSROOM_ID));
        this.date = getArguments().getString(DATE);
        Log.i(TAG, this.classroom.getName());
        studentRecords = new ArrayList<StudentRecord>();
        buildAdapter();
    }

    public void onListItemClick(ListView listView, View view, int position, long id) {
        Log.i(TAG, "HIT");
    }

    private void buildAdapter() {
        /* There are currently two adapters to choose from. New or Premade
        * The Premade adapter gets chosen if the date and classroom provided when creating this
        * fragment find a match within the StudentRecords database. Otherwise, the new adapter is chosen.*/
        ArrayList<StudentRecord> studentRecordDateMatch = (ArrayList<StudentRecord>) repo.studentRecords.findByDate(this.date);
        Log.i(TAG, "For the date: " + this.date + ", studentRecordDateMatch size is: " + studentRecordDateMatch.size());

        ArrayList<StudentRecord> studentRecordsMatchClassroomAndDate = new ArrayList<StudentRecord>();

        for (int i = 0; i < studentRecordDateMatch.size(); i++) {
            if (studentRecordDateMatch.get(i).getClassroom().getId() == this.classroom.getId()) {
                Log.i(TAG, "MATCH: " + studentRecordDateMatch.get(i).toString());
                studentRecordsMatchClassroomAndDate.add(studentRecordDateMatch.get(i));
            }
        }

        if(studentRecordsMatchClassroomAndDate.size() >= 1) {
            adapter = new PremadeStudentRecordAdapter(getActivity(), studentRecordsMatchClassroomAndDate);
        } else {
            studentList = (ArrayList<Student>) classroom.getStudentsAsList();
            Collections.sort(studentList, new StudentSortByName());
            adapter = new NewStudentRecordAdapter(getActivity(), studentList);
        }
        setListAdapter(adapter);

    }

    private class ViewHolder {
        ImageView image;
        TextView englishName;
        TextView koreanName;
        CheckBox homework;
        CheckBox attendance;
        Button notes;
    }

    private class PremadeStudentRecordAdapter extends ArrayAdapter<StudentRecord> {
        private Context context;

        public PremadeStudentRecordAdapter(Context context, ArrayList<StudentRecord> studentRecords) {
            super(context, android.R.layout.simple_list_item_1, studentRecords);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.fragment_student_record_create, parent, false);

            StudentRecord sr = getItem(position);
            studentRecords.add(sr);

            holder = new ViewHolder();
            holder.image = (ImageView) rowView.findViewById(R.id.image);
            Picasso.with(getContext()).load(Uri.parse(sr.getStudent().getPictureUri())).error(R.drawable.avatar_blank).placeholder(R.drawable.avatar_blank).fit().centerCrop().into(holder.image);

            holder.koreanName = (TextView) rowView.findViewById(R.id.fragment_student_record_korean_name);
            holder.englishName = (TextView) rowView.findViewById(R.id.fragment_student_record_english_name);
            holder.attendance = (CheckBox) rowView.findViewById(R.id.fragment_student_record_attendance);
            holder.homework = (CheckBox) rowView.findViewById(R.id.fragment_student_record_homework);
            holder.notes = (Button) rowView.findViewById(R.id.fragment_student_record_add_notes);
            rowView.setTag(holder);

            holder.attendance.setChecked(sr.isHasAttended());
            holder.homework.setChecked(sr.isHasHomework());

            holder.attendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        StudentRecord sr = (StudentRecord) buttonView.getTag();
                        for (int i = 0; i < studentRecords.size(); i++) {
                            if (studentRecords.get(i).equals(sr)) {
                                studentRecords.get(i).setHasAttended(true);
                            }
                        }
                    } else {
                        StudentRecord sr = (StudentRecord) buttonView.getTag();
                        for (int i = 0; i < studentRecords.size(); i++) {
                            if (studentRecords.get(i).equals(sr)) {
                                studentRecords.get(i).setHasAttended(false);
                            }
                        }
                    }
                }
            });

            holder.homework.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        StudentRecord sr = (StudentRecord) buttonView.getTag();
                        for (int i = 0; i < studentRecords.size(); i++) {
                            if (studentRecords.get(i).equals(sr)) {
                                studentRecords.get(i).setHasHomework(true);
                            }
                        }
                    } else {
                        StudentRecord sr = (StudentRecord) buttonView.getTag();
                        for (int i = 0; i < studentRecords.size(); i++) {
                            if (studentRecords.get(i).equals(sr)) {
                                studentRecords.get(i).setHasHomework(false);
                            }
                        }
                    }
                }
            });
            holder.koreanName.setText(sr.getStudent().getKoreanName());
            holder.englishName.setText(sr.getStudent().getEnglishName());
            holder.image.setTag(sr);
            holder.englishName.setTag(sr);
            holder.koreanName.setTag(sr);
            holder.attendance.setTag(sr);
            holder.homework.setTag(sr);
            holder.notes.setTag(sr);
            return rowView;
        }
    }

    private class NewStudentRecordAdapter extends ArrayAdapter<Student> {
        private final Context context;
        private final ArrayList<Student> students;

        public NewStudentRecordAdapter(Context context, ArrayList<Student> students) {
            super(context, android.R.layout.simple_list_item_1, students);
            this.context = context;
            this.students = students;
            studentRecords = new ArrayList<StudentRecord>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.fragment_student_record_create, parent, false);

            Student s = getItem(position);
            StudentRecord sr = new StudentRecord();
            sr.setMessage("");
            sr.setStudent(s);
            sr.setClassroom(classroom);
            studentRecords.add(sr);

            holder = new ViewHolder();
            holder.image = (ImageView) rowView.findViewById(R.id.image);
            Picasso.with(getContext()).load(Uri.parse(sr.getStudent().getPictureUri())).error(R.drawable.avatar_blank).placeholder(R.drawable.avatar_blank).fit().centerCrop().into(holder.image);
            holder.koreanName = (TextView) rowView.findViewById(R.id.fragment_student_record_korean_name);
            holder.englishName = (TextView) rowView.findViewById(R.id.fragment_student_record_english_name);
            holder.attendance = (CheckBox) rowView.findViewById(R.id.fragment_student_record_attendance);
            holder.homework = (CheckBox) rowView.findViewById(R.id.fragment_student_record_homework);
            holder.notes = (Button) rowView.findViewById(R.id.fragment_student_record_add_notes);
            rowView.setTag(holder);

            holder.attendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Student s = (Student) buttonView.getTag();
                        for (int i = 0; i < studentRecords.size(); i++) {
                            if (studentRecords.get(i).getStudent().equals(s)) {
                                studentRecords.get(i).setHasAttended(true);
                            }
                        }
                    } else {
                        Student s = (Student) buttonView.getTag();
                        for (int i = 0; i < studentRecords.size(); i++) {
                            if (studentRecords.get(i).getStudent().equals(s)) {
                                studentRecords.get(i).setHasAttended(false);
                            }
                        }
                    }
                }
            });

            holder.homework.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Student s = (Student) buttonView.getTag();
                        for (int i = 0; i < studentRecords.size(); i++) {
                            if (studentRecords.get(i).getStudent().equals(s)) {
                                studentRecords.get(i).setHasHomework(true);
                            }
                        }
                    } else {
                        Student s = (Student) buttonView.getTag();
                        for (int i = 0; i < studentRecords.size(); i++) {
                            if (studentRecords.get(i).getStudent().equals(s)) {
                                studentRecords.get(i).setHasHomework(false);
                            }
                        }
                    }
                }
            });
            holder.koreanName.setText(sr.getStudent().getKoreanName());
            holder.englishName.setText(sr.getStudent().getEnglishName());
            holder.image.setTag(sr);
            holder.englishName.setTag(sr);
            holder.koreanName.setTag(sr);
            holder.attendance.setTag(s);
            holder.homework.setTag(s);
            holder.notes.setTag(sr);
            return rowView;
        }
    }

    public ArrayList<StudentRecord> getStudentRecords() {
        return this.studentRecords;
    }
}
