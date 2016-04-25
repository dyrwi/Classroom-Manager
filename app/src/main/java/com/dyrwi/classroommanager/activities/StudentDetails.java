/*
package com.dyrwi.classroommanager.activities;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.fragments.StudentDetailsFrag;
import com.dyrwi.classroommanager.fragments.StudentRecordList;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.repository.Repo;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

*/
/**
 * Created by Ben on 14-Oct-15.
 *//*

public class StudentDetails extends AppCompatActivity {
    private static final String TAG = "StudentDetails";
    public static String ID = "id";
    private Toolbar toolbar;
    private Repo repo;
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private long studentID;
    private Student student;

    // Widgets
    private ImageView mImage;
    private TextView mEnglishName;
    private TextView mKoreanName;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.activity_student_details);

        repo = new Repo(this);
        studentID = getIntent().getLongExtra(ID, 0);
        student = repo.students.findByID(studentID);

        // Couldn't find student
        if(student == null) {
            Toast.makeText(this, "Could not find student", Toast.LENGTH_SHORT).show();
            finish();
        }

        mImage = (ImageView)findViewById(R.id.image);
        Picasso.with(this).load(Uri.parse(student.getPictureUri())).placeholder(R.drawable.avatar_blank).error(R.drawable.avatar_blank).into(mImage);

        mEnglishName = (TextView)findViewById(R.id.english_name);
        mEnglishName.setText(student.getEnglishName());
        mKoreanName = (TextView)findViewById(R.id.korean_name);
        mKoreanName.setText(student.getKoreanName());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back).colorRes(R.color.white).sizeDp(16));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Student Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onBackPressed();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.fragment_content);
        viewPager.setAdapter(new StudentDetailsPager(getSupportFragmentManager()));

        tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);
    }

    private class StudentDetailsPager extends FragmentPagerAdapter {
        public int NUM_OF_ITEMS = 2;
        private String[] tabTitles = new String[]{"About", "Records"};

        public StudentDetailsPager(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_OF_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return StudentDetailsFrag.newInstance(studentID);
                case 1:
                    return StudentRecordList.newInstance(studentID);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
*/
