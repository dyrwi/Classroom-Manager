package com.dyrwi.classroommanager.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.repository.Repo;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 14-Oct-15.
 */
public class MaterialStudentDetails extends AppCompatActivity {
    private static final String TAG = "MatStudentDetails";
    private static int UPDATE = 1;
    public static String ID = "id";
    private static int ICON_COLOR = R.color.colorPrimary;

    // Java
    private Toolbar toolbar;
    private Repo repo;
    private long studentID;
    private Student student;

    // Widgets
    private ImageView mImage;
    private TextView mEnglishName;
    private TextView mKoreanName;
    private TextView mPhoneNumber;
    private TextView mDateOfBirth;
    private TextView mClassroom;
    private TextView mMessage;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.material_student_details);

        repo = new Repo(this);
        studentID = getIntent().getLongExtra(ID, 0);
        student = repo.students.findByID(studentID);

        // Couldn't find student
        if (student == null) {
            Toast.makeText(this, "Could not find student", Toast.LENGTH_SHORT).show();
            finish();
        }
        setWidgets();
        repo = new Repo(this);
    }

    private void setWidgets() {
        /**
         * Toolbar Setup
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back).colorRes(R.color.white).sizeDp(16));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(student.getKoreanAndEnglishNames());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(student.getKoreanAndEnglishNames());

        /**
         * Content Setup
         */

        mImage = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(Uri.parse(student.getPictureUri())).placeholder(R.drawable.avatar_blank).error(R.drawable.avatar_blank).fit().centerCrop().into(mImage);

        /**
         * Currently disabled. You need to re enable in the xml file and also this file here.
         */
        /*ImageView mEnglishNameIcon = (ImageView)findViewById(R.id.english_name_icon);
        mEnglishNameIcon.setImageDrawable(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_alphabetical).colorRes(ICON_COLOR).sizeDp(24));
        mEnglishName = (TextView)findViewById(R.id.english_name_data);
        mEnglishName.setText(student.getEnglishName());

        ImageView mKoreanNameIcon = (ImageView)findViewById(R.id.korean_name_icon);
        mKoreanNameIcon.setImageDrawable(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_alphabetical).colorRes(ICON_COLOR).sizeDp(24));
        mKoreanName = (TextView)findViewById(R.id.korean_name_data);
        mKoreanName.setText(student.getKoreanName());*/

        ImageView mPhoneNumberIcon = (ImageView)findViewById(R.id.phone_icon);
        mPhoneNumberIcon.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_phone).colorRes(ICON_COLOR).sizeDp(24));
        mPhoneNumber = (TextView)findViewById(R.id.phone_data);
        mPhoneNumber.setText(student.getPhoneNumber());

        ImageView mDateOfBirthIcon = (ImageView)findViewById(R.id.date_of_birth_icon);
        mDateOfBirthIcon.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_cake).colorRes(ICON_COLOR).sizeDp(24));
        mDateOfBirth = (TextView)findViewById(R.id.date_of_birth_data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy, MM, dd");
        mDateOfBirth.setText(sdf.format(student.getDateOfBirth().getTime()));

        ImageView mClassroomIcon = (ImageView)findViewById(R.id.classroom_icon);
        mClassroomIcon.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_class).colorRes(ICON_COLOR).sizeDp(24));
        mClassroom = (TextView)findViewById(R.id.classroom_data);
        mClassroom.setText(student.getClassroom().getName());

        ImageView mMessageIcon = (ImageView)findViewById(R.id.message_icon);
        mMessageIcon.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_message).colorRes(ICON_COLOR).sizeDp(24));
        mMessage = (TextView)findViewById(R.id.message_data);
        mMessage.setText(student.getMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_details, menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        MenuItem mEdit = menu.findItem(R.id.edit);
        mEdit.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_edit).colorRes(R.color.white).sizeDp(16));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.edit:
                editStudent();
                return true;
            default:
                return true;
        }
    }

    private void editStudent() {
        Intent i = new Intent(MaterialStudentDetails.this, AddEditStudent.class);
        i.putExtra(AddEditStudent.STUDENT_ID, student.getId());
        startActivityForResult(i, UPDATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == UPDATE) {
                // Get the newly saved student. It has the same ID, but we
                // need the new reference.
                Log.i(TAG, "onActivityResult()");
                student = repo.students.findByID(studentID);
                setWidgets();

                // Update the StudentList Fragment
                sendBroadcast(new Intent("StudentList"));
            }
        }
    }
}
