package com.dyrwi.classroommanager.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.repository.Repo;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Ben on 07-Oct-15.
 */
public class SimpleStudentListFrag extends ListFragment {
    public static final String STUDENT = "STUDENT";
    private static final String TAG = "SimpleStudentListFrag";
    private StudentAdapter adapter;
    private ArrayList<Student> studentList;
    private Student selectedStudent;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Repo repo;
    private Menu menu;
    private View view;

    public static SimpleStudentListFrag newInstance() {
        return new SimpleStudentListFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        repo = new Repo(getActivity());
        studentList = new ArrayList<Student>();
        adapter = new StudentAdapter(getActivity(), studentList);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_to_refresh, null);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        fetchStudents();
                                    }
                                }
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchStudents();
            }
        });
        return view;
    }

    private void fetchStudents() {
        swipeRefreshLayout.setRefreshing(true);
        ArrayList<Student> tempStudentList = (ArrayList<Student>)repo.students.getAll();
        for(int i = 0; i < tempStudentList.size(); i++) {
            studentList.add(tempStudentList.get(i));
        }
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceStatus) {
        super.onActivityCreated(savedInstanceStatus);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.student_list, menu);
        this.menu = menu;
        MenuItem searchItem = menu.findItem(R.id.menu_student_list_search);
        searchItem.setIcon(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_search).color(Color.GRAY).sizeDp(20));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final SearchView searchView = (SearchView) menu.findItem(R.id.menu_student_list_search).getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    search(query);
                    return true;
                }
            });
        }
    }

    private void search(String query) {
        Log.i(TAG, "Searching " + query);
        ArrayList<Student> tempStudentList = new ArrayList<Student>();
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getKoreanAndEnglishNames().toLowerCase().contains(query.toLowerCase())) {
                tempStudentList.add(studentList.get(i));
            }
        }
        Log.i(TAG, "Search has found: " + String.valueOf(tempStudentList.size()) + " correct matches");
        adapter = new StudentAdapter(getActivity(), tempStudentList);
        setListAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_student_list_order_by_english_name:
                sortByEnglishName();
                return true;
            case R.id.menu_student_list_order_by_korean_name:
                sortByKoreanName();
                return true;
            case R.id.menu_student_list_order_by_age_asc:
                sortByAgeAscending();
                return true;
            case R.id.menu_student_list_order_by_age_desc:
                sortByAgeDescending();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void sortByAgeDescending() {

    }

    private void sortByAgeAscending() {

    }

    private void sortByKoreanName() {
    }

    private void sortByEnglishName() {

    }

    public void onListItemClick(ListView listView, View view, int position, long id) {
        if (selectedStudent == null) {
            selectedStudent = new Student();
        }
        selectedStudent = (Student) adapter.getItem(position);
        Log.i(TAG, selectedStudent.getEnglishName() + " is selected");
        Intent returnIntent = new Intent();
        returnIntent.putExtra(STUDENT, selectedStudent.getId());
        Log.i(TAG, getActivity().toString());
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }


    private class StudentAdapter extends ArrayAdapter<Student> {
        private final Context context;
        private final ArrayList<Student> students;

        public StudentAdapter(Context context, ArrayList<Student> students) {
            super(context, android.R.layout.simple_list_item_1, students);
            this.context = context;
            this.students = students;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.fragment_simple_student_list, parent, false);
            Student s = getItem(position);
            TextView englishName = (TextView) rowView.findViewById(R.id.student_list_english_name);
            englishName.setText(s.getEnglishName());

            TextView koreanName = (TextView) rowView.findViewById(R.id.student_list_korean_name);
            koreanName.setText(s.getKoreanName());

            TextView dateOfBirth = (TextView) rowView.findViewById(R.id.student_list_date_of_birth);
            dateOfBirth.setText(DateFormat
                    .getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
                    .format(s.getDateOfBirth()));

            return rowView;
        }
    }
}
