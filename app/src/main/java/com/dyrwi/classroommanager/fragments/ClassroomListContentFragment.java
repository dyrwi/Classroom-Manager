package com.dyrwi.classroommanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.activities.ClassroomDetails;
import com.dyrwi.classroommanager.activities.MaterialClassroomDetails;
import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.repository.Repo;
import com.dyrwi.classroommanager.util.ClassroomSort;
import com.dyrwi.classroommanager.util.RecyclerViewEmptySupport;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ben on 03-Nov-15.
 */
public class ClassroomListContentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ClsrmListContentFrag";
    private Repo repo;
    private List<Classroom> classrooms;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerViewEmptySupport recyclerView;
    private ClassroomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        repo = new Repo(getActivity());
        classrooms = repo.classrooms.getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {
        View view = inflater.inflate(R.layout.swipe_recycler_view, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new ClassroomAdapter(getContext(), classrooms);

        recyclerView = (RecyclerViewEmptySupport) view.findViewById(R.id.recycler_view);
        TextView emptyMessage = (TextView) view.findViewById(R.id.empty_message);
        emptyMessage.setText(R.string.no_classrooms);
        recyclerView.setEmptyView(view.findViewById(R.id.empty));

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
        return view;

    }

    @Override
    public void onRefresh() {
        ArrayList<Classroom> tempClassrooms = (ArrayList<Classroom>) repo.classrooms.getAll();
        swipeRefreshLayout.setRefreshing(true);
        classrooms.clear();
        for (int i = 0; i < tempClassrooms.size(); i++)
            classrooms.add(tempClassrooms.get(i));
        Collections.sort(classrooms, new ClassroomSort.byName());
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    private class ClassroomAdapter extends RecyclerView.Adapter<ClassroomHolder> {
        private List<Classroom> classroomList;
        private Context context;

        public ClassroomAdapter(Context context, List<Classroom> classroomList) {
            this.classroomList = classroomList;
            this.context = context;
        }

        @Override
        public ClassroomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_classroom, null);
            return new ClassroomHolder(view);
        }

        @Override
        public void onBindViewHolder(ClassroomHolder holder, int position) {
            Classroom classroom = classroomList.get(position);
            Picasso.with(context).load(Uri.parse(classroom.getPictureUri()))
                    .placeholder(R.drawable.avatar_blank)
                    .error(R.drawable.avatar_blank)
                    .fit()
                    .centerCrop()
                    .into(holder.image);
            holder.name.setText(classroom.getName());
            holder.bindClassroomId(classroom.getId());
            //holder.bindClassroom(classroom);
        }

        @Override
        public int getItemCount() {
            return (classroomList != null ? classroomList.size() : 0);
        }
    }

    private class ClassroomHolder extends RecyclerView.ViewHolder {
        //private Classroom classroom;
        private long id;
        private ImageView image;
        private TextView name;

        public ClassroomHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), MaterialClassroomDetails.class);
                    i.putExtra(MaterialClassroomDetails.ID, id);
                    startActivity(i);
                }
            });
        }

        public void bindClassroom(Classroom classroom) {
            //this.classroom = classroom;
        }

        public void bindClassroomId(long id) {
            this.id = id;
        }
    }
}
