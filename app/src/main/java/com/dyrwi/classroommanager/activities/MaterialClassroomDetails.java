package com.dyrwi.classroommanager.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.repository.Repo;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

/**
 * Created by Ben on 04-Feb-16.
 */
public class MaterialClassroomDetails extends AppCompatActivity {
    private static final String TAG = "MatClassroomDetails";
    private static int UPDATE = 1;
    public static String ID = "id";
    private static int ICON_COLOR = R.color.colorPrimary;

    // Java
    private Toolbar toolbar;
    private Repo repo;
    private long classroomID;
    private Classroom classroom;

    // Widgets
    private ImageView mImage;
    private TextView mPhoneNumber;
    private TextView mDateOfBirth;
    private TextView mClassroom;
    private TextView mMessage;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.material_student_details);

        repo = new Repo(this);
        classroomID = getIntent().getLongExtra(ID, 0);
        classroom = repo.classrooms.findByID(classroomID);

        // Couldn't find classroom
        if (classroom == null) {
            Toast.makeText(this, "Could not find classroom", Toast.LENGTH_SHORT).show();
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
        getSupportActionBar().setTitle(classroom.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(classroom.getName());

        /**
         * Content Setup
         */

        mImage = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(Uri.parse(classroom.getPictureUri())).placeholder(R.drawable.avatar_blank).error(R.drawable.avatar_blank).fit().centerCrop().into(mImage);

        ImageView mPhoneNumberIcon = (ImageView)findViewById(R.id.phone_icon);
        mPhoneNumberIcon.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_phone).colorRes(ICON_COLOR).sizeDp(24));
        mPhoneNumber = (TextView)findViewById(R.id.phone_data);
        mPhoneNumber.setText(classroom.getEnglishNamesOfStudents());

        ImageView mDateOfBirthIcon = (ImageView)findViewById(R.id.date_of_birth_icon);
        mDateOfBirthIcon.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_cake).colorRes(ICON_COLOR).sizeDp(24));
        mDateOfBirth = (TextView)findViewById(R.id.date_of_birth_data);
        mDateOfBirth.setText(classroom.getKoreanNamesOfStudents());
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
                editClassroom();
                return true;
            default:
                return true;
        }
    }

    private void editClassroom() {
       /* Intent i = new Intent(MaterialClassroomDetails.this, AddEditStudent.class);
        i.putExtra(AddEditStudent.STUDENT_ID, student.getId());
        startActivityForResult(i, UPDATE);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == UPDATE) {
                // Get the newly saved student. It has the same ID, but we
                // need the new reference.
                Log.i(TAG, "onActivityResult()");
                classroom = repo.classrooms.findByID(classroomID);
                setWidgets();
            }
        }
    }
}
