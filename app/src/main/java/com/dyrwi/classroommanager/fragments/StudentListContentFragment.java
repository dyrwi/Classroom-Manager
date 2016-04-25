package com.dyrwi.classroommanager.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.activities.MaterialStudentDetails;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.repository.Repo;
import com.dyrwi.classroommanager.util.RecyclerViewEmptySupport;
import com.dyrwi.classroommanager.util.StudentSortByName;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ben on 03-Nov-15.
 */
public class StudentListContentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "StudentListContentFrag";
    private Repo repo;
    private List<Student> students;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerViewEmptySupport recyclerView;
    private StudentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        repo = new Repo(getActivity());
        students = repo.students.getAll();
        getActivity().registerReceiver(new Receiver(), new IntentFilter("StudentList"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {
        View view = inflater.inflate(R.layout.swipe_recycler_view, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new StudentAdapter(getContext(), students);
        recyclerView = (RecyclerViewEmptySupport) view.findViewById(R.id.recycler_view);

        TextView emptyMessage = (TextView) view.findViewById(R.id.empty_message);
        emptyMessage.setText(R.string.no_students);
        recyclerView.setEmptyView(view.findViewById(R.id.empty));

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

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
        Log.i(TAG, "fetching students");
        ArrayList<Student> tempStudentList = (ArrayList<Student>) repo.students.getAll();
        swipeRefreshLayout.setRefreshing(true);
        students.clear();
        for (int i = 0; i < tempStudentList.size(); i++)
            students.add(tempStudentList.get(i));
        Collections.sort(students, new StudentSortByName());
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentHolder> {
        private List<Student> studentList;
        private Context context;

        public StudentAdapter(Context context, List<Student> studentList) {
            this.studentList = studentList;
            this.context = context;
        }

        @Override
        public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student, null);
            return new StudentHolder(view);
        }

        @Override
        public void onBindViewHolder(StudentHolder holder, int position) {
            Student s = studentList.get(position);
            Picasso.with(context).load(Uri.parse(s.getPictureUri()))
                    .placeholder(R.drawable.avatar_blank)
                    .error(R.drawable.avatar_blank)
                    .noFade()
                    .fit()
                    .centerCrop()
                    .into(holder.image);
            holder.englishName.setText(s.getEnglishName());
            holder.koreanName.setText(s.getKoreanName());
            holder.bindStudentId(s.getId());
        }

        @Override
        public int getItemCount() {
            return (students != null ? students.size() : 0);
        }
    }

    private class StudentHolder extends RecyclerView.ViewHolder {
        private long studentId;
        private ImageView image;
        private TextView englishName;
        private TextView koreanName;
        private RelativeLayout container;

        public StudentHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            englishName = (TextView) itemView.findViewById(R.id.english_name);
            koreanName = (TextView) itemView.findViewById(R.id.korean_name);
            container = (RelativeLayout) itemView.findViewById(R.id.container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), MaterialStudentDetails.class);
                    i.putExtra(MaterialStudentDetails.ID, studentId);
                    startActivity(i);
                }
            });
        }


        public void bindStudentId(long id) {
            this.studentId = id;
        }
    }

    /**
     * Receiver used so we can update the list when the user adds
     * a new student from the AddEditStudent class. There may be a better
     * way to implement this, however I do not know it.
     */
    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    }


}
