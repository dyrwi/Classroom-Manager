package com.dyrwi.classroommanager.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.dyrwi.classroommanager.activities.ClassroomDetails;
import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.repository.Repo;
import com.dyrwi.classroommanager.util.ClassroomSort;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ben on 14-Oct-15.
 */
public class AdvancedClassroomListFrag extends ListFragment {
    private static final String TAG = "AdvancedClassroomList";

    private ArrayList<Classroom> classroomList;
    private ClassroomListAdapter adapter;
    private Repo repo;
    private Menu menu;
    private String searchQuery;
    private ArrayList<Classroom> repoClassrooms;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static AdvancedClassroomListFrag newInstance() {
        AdvancedClassroomListFrag fragment = new AdvancedClassroomListFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        this.repo = new Repo(getActivity());
        searchQuery = "";
        classroomList = new ArrayList<Classroom>();
        repoClassrooms = (ArrayList<Classroom>) repo.classrooms.getAll();
        for (int i = 0; i < repoClassrooms.size(); i++) {
            classroomList.add(repoClassrooms.get(i));
        }
        Collections.sort(classroomList, new ClassroomSort.byName());
        adapter = new ClassroomListAdapter(getActivity(), classroomList);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {
        View view = inflater.inflate(R.layout.swipe_to_refresh, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         mSwipeRefreshLayout.setRefreshing(true);
                                         fetchClassrooms();
                                     }
                                 }
        );
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /* If the user pulls down while they are searching, it will reload the search options.
             */
            @Override
            public void onRefresh() {
                if (!searchQuery.toLowerCase().equalsIgnoreCase(""))
                    search(searchQuery);
                else
                    fetchClassrooms();
            }
        });
        return view;
    }

    private void fetchClassrooms() {
        mSwipeRefreshLayout.setRefreshing(true);
        ArrayList<Classroom> tempClassroomList = (ArrayList<Classroom>) repo.classrooms.getAll();
        classroomList.clear();
        for (int i = 0; i < tempClassroomList.size(); i++) {
            classroomList.add(tempClassroomList.get(i));
        }
        Collections.sort(classroomList, new ClassroomSort.byName());
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceStatus) {
        super.onActivityCreated(savedInstanceStatus);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.classroom_list, menu);
        this.menu = menu;
        MenuItem searchItem = menu.findItem(R.id.menu_classroom_list_search);
        searchItem.setIcon(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_search).color(Color.GRAY).sizeDp(20));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final SearchView searchView = (SearchView) menu.findItem(R.id.menu_classroom_list_search).getActionView();
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
        mSwipeRefreshLayout.setRefreshing(true);
        ArrayList<Classroom> tempClassroomList = new ArrayList<Classroom>();
        for (int i = 0; i < classroomList.size(); i++) {
            if (classroomList.get(i).toString().toLowerCase().contains(query.toLowerCase())) {
                tempClassroomList.add(classroomList.get(i));
            }
        }
        classroomList.clear();
        for (int i = 0; i < tempClassroomList.size(); i++) {
            classroomList.add(tempClassroomList.get(i));
        }
        mSwipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    public void onListItemClick(ListView listView, View view, int position, long id) {
        Intent i = new Intent(getActivity(), ClassroomDetails.class);
        Classroom c = (Classroom)adapter.getItem(position);
        i.putExtra(ClassroomDetailsFrag.ID, c .getId());
        startActivity(i);
    }

    private class ClassroomListAdapter extends BaseAdapter {
        private Activity mContext;
        private List<Classroom> mList;
        private LayoutInflater mLayoutInflater = null;

        public ClassroomListAdapter(Activity context, List<Classroom> list) {
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
            View rowView = convertView;
            if (convertView == null) {
                ClassroomListViewHolder viewHolder;
                mLayoutInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = mLayoutInflater.inflate(R.layout.empty_list, null);
                viewHolder = new ClassroomListViewHolder(rowView);
                rowView.setTag(viewHolder);
            }
            ClassroomListViewHolder holder = (ClassroomListViewHolder) rowView.getTag();
            //Picasso.with(getContext()).load(Uri.parse(mList.get(position).getPictureUri())).error(R.drawable.avatar_blank).placeholder(R.drawable.avatar_blank).fit().centerCrop().into(holder.mImage);
            holder.mName.setText(mList.get(position).getName());
            //holder.mKoreanNames.setText(mList.get(position).getKoreanNamesOfStudents());
            return rowView;
        }
    }

    private class ClassroomListViewHolder {
        public ImageView mImage;
        public TextView mName;
        public TextView mKoreanNames;

        public ClassroomListViewHolder(View base) {
            //mImage = (ImageView) base.findViewById(R.id.fragment_advanced_classroom_list_image);
            mName = (TextView) base.findViewById(R.id.empty_list);
            //mKoreanNames = (TextView) base.findViewById(R.id.fragment_advanced_classroom_list_student_korean_names);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
