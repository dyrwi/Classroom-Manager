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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.activities.MaterialStudentDetails;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.repository.Repo;
import com.dyrwi.classroommanager.util.StudentSortByName;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ben on 14-Oct-15.
 */
public class AdvancedStudentListFrag extends ListFragment {
    private static final String TAG = "AdvancedStudentListFrag";

    private StudentListAdapter adapter;
    private ArrayList<Student> studentList;
    private Repo repo;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Menu menu;
    private Activity mContext;

    private ArrayList<Student> repoStudents;
    private String searchQuery;

    public static AdvancedStudentListFrag newInstance() {
        AdvancedStudentListFrag fragment = new AdvancedStudentListFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        repo = new Repo(getActivity());
        mContext = getActivity();
        searchQuery = "";
        studentList = new ArrayList<Student>();
        repoStudents = (ArrayList<Student>) repo.students.getAll();
        Collections.sort(repoStudents, new StudentSortByName());
        for (int i = 0; i < repoStudents.size(); i++) {
            studentList.add(repoStudents.get(i));
        }
        adapter = new StudentListAdapter(getActivity(), studentList);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_to_refresh, null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        fetchStudents();
                                    }
                                }
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /* If the user pulls down while they are searching, it will reload the search options.
             */
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh: Refreshing student list");
                if (!searchQuery.toLowerCase().equalsIgnoreCase(""))
                    search(searchQuery);
                else
                    fetchStudents();
            }
        });
        return view;
    }

    private void fetchStudents() {
        swipeRefreshLayout.setRefreshing(true);
        Log.i(TAG, "fetching students");
        ArrayList<Student> tempStudentList = (ArrayList<Student>) repo.students.getAll();
        studentList.clear();
        for (int i = 0; i < tempStudentList.size(); i++) {
            studentList.add(tempStudentList.get(i));
        }
        Collections.sort(studentList, new StudentSortByName());
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceStatus) {
        super.onActivityCreated(savedInstanceStatus);
        setHasOptionsMenu(true);
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
        searchQuery = query;
        swipeRefreshLayout.setRefreshing(true);
        Log.i(TAG, "Searching " + query);
        ArrayList<Student> tempStudentList = new ArrayList<Student>();
        for (int i = 0; i < repoStudents.size(); i++) {
            if (repoStudents.get(i).getKoreanAndEnglishNames().toLowerCase().contains(query.toLowerCase())) {
                tempStudentList.add(repoStudents.get(i));
            }
        }
        studentList.clear();
        for (int i = 0; i < tempStudentList.size(); i++) {
            studentList.add(tempStudentList.get(i));
        }
        Log.i(TAG, "Search has found: " + String.valueOf(tempStudentList.size()) + " correct matches");
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
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
        Student s = (Student) adapter.getItem(position);
        Intent i = new Intent(getActivity(), MaterialStudentDetails.class);
        i.putExtra(MaterialStudentDetails.ID, s.getId());
        startActivity(i);
    }

    private class StudentListAdapter extends BaseAdapter {

        private ArrayList<Student> mList;
        private LayoutInflater mLayoutInflater = null;

        public StudentListAdapter(Activity context, ArrayList<Student> list) {
            Log.i(TAG, "Creating StudentListAdapter");
            mContext = context;
            mList = list;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int pos) {
            return mList.get(pos);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "StudentListAdapter getView");
            View rowView = convertView;
            if (rowView == null) {
/*                Log.i(TAG, "convertView == null");*/
                mLayoutInflater = mContext.getLayoutInflater();
                rowView = mLayoutInflater.inflate(R.layout.empty_list, null);
                StudentListViewHolder viewHolder = new StudentListViewHolder(rowView);

                //viewHolder.mImage = (ImageView) rowView.findViewById(R.id.fragment_advanced_student_list_image);
                viewHolder.mEnglishName = (TextView) rowView.findViewById(R.id.empty_list);
                //viewHolder.mKoreanName = (TextView) rowView.findViewById(R.id.fragment_advanced_student_list_korean_name);

                viewHolder.mEnglishName.setTextSize(getResources().getDimension(R.dimen.name_header));
                //viewHolder.mKoreanName.setTextSize(getResources().getDimension(R.dimen.student_list_korean_name));
                rowView.setTag(viewHolder);
            }
            StudentListViewHolder holder = (StudentListViewHolder) rowView.getTag();
            holder.mEnglishName.setText(mList.get(position).getEnglishName());
            //holder.mKoreanName.setText(mList.get(position).getKoreanName());
            return rowView;
        }
    }

    private class StudentListViewHolder {
        public ImageView mImage;
        public TextView mEnglishName;
        public TextView mKoreanName;

        public StudentListViewHolder(View base) {

        }
    }
}
