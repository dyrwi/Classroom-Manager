package com.dyrwi.classroommanager.activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.fragments.ClassroomRecordListFrag;
import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.model.StudentRecord;
import com.dyrwi.classroommanager.repository.Repo;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Ben on 19-Oct-15.
 */
public class ClassroomRecordActivity extends AppCompatActivity {
    // Static
    private static final String TAG = "ClassroomRecordActivity";

    // Widgets
    private Spinner mSpinner;
    private Toolbar toolbar;
    private Button mSave;
    private Button mChooseDate;
    private Button mCancel;

    // Java
    private Calendar date = Calendar.getInstance();
    private Calendar datePickerCalendar = Calendar.getInstance();
    private Classroom classroom;
    private Calendar calendar = Calendar.getInstance();
    private String dateSelected = "";

    // Other
    private Repo repo;
    private ClassroomRecordListFrag fragment;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        this.repo = new Repo(this);
        setContentView(R.layout.classroom_record);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back).colorRes(R.color.white).sizeDp(16));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle("Classroom Record");
        setWidgets();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.confirm:
                saveRecord();
                return true;
            default:
                return true;
        }
    }

    private void saveRecord() {
        if (fragment == null) {
            Toast.makeText(getApplicationContext(), "No classsroom record to save", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<StudentRecord> studentRecords = fragment.getStudentRecords();
            for (StudentRecord sr : studentRecords) {
                Log.i(TAG, "Date is set to: " + dateSelected);
                sr.setDate(dateSelected);
                repo.studentRecords.createOrUpdate(sr);
                Student s = repo.students.findByID(sr.getStudent().getId());
                repo.students.update(s);
            }
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_student, menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        MenuItem mConfirm = menu.findItem(R.id.confirm);
        mConfirm.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_save).colorRes(R.color.white).sizeDp(16));
        return true;
    }

    private void setWidgets() {
        mSpinner = (Spinner) findViewById(R.id.classroom_record_spinner);
        ArrayList<String> classroomSpinnerList = new ArrayList<String>();
        for (Classroom c : repo.classrooms.getAll()) {
            classroomSpinnerList.add(c.getName());
        }
        mChooseDate = (Button) findViewById(R.id.classroom_record_date);
        Log.i(TAG, "Calendar: " + Calendar.YEAR + "-" + Calendar.MONTH + "-" + Calendar.DAY_OF_MONTH);
        showDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        mChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment dateFragment = new DateDialogFragment();
                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });
        mSpinner.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, classroomSpinnerList));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classroom = (Classroom) repo.classrooms.findByName((String) parent.getItemAtPosition(position));
                if (classroom != null && date != null) {
                    buildFragment();
                }
                if (classroom == null)
                    Toast.makeText(getApplicationContext(), "No classroom found", Toast.LENGTH_SHORT).show();
                else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void buildFragment() {
        if (fragment != null) {
            FragmentTransaction removeFrag = getSupportFragmentManager().beginTransaction();
            removeFrag.remove(fragment).commit();
        }
        Log.i(TAG, "Fragment is getting built");
        Log.i(TAG, "Classroom ID: " + classroom.getId() + " Date: " + date.toString());
        fragment = new ClassroomRecordListFrag().newInstance(classroom.getId(), dateSelected);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.classroom_record_contents, fragment).commit();
    }

    private class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, DatePicker.OnDateChangedListener {
        private DatePickerDialog fragment;
        private DatePicker picker;
        private Calendar c;
        private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d, yyyy");

        public DateDialogFragment() {
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = datePickerCalendar.get(Calendar.YEAR);
            int month = datePickerCalendar.get(Calendar.MONTH);
            int day = datePickerCalendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            fragment = new DatePickerDialog(getActivity(), this, year, month, day);
            c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

            fragment.setTitle(sdf.format(c.getTime()));
            picker = fragment.getDatePicker();
            picker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, monthOfYear);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    fragment.setTitle(sdf.format(c.getTime()));
                }
            });
            return fragment;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.i(TAG, "onDateSet");
            showDate(year, monthOfYear, dayOfMonth);
            checkDateInStudentRecords();
            // When the user re selects the date.
            // It will show the last date they set.
            datePickerCalendar.set(Calendar.YEAR, year);
            datePickerCalendar.set(Calendar.MONTH, monthOfYear);
            datePickerCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.i(TAG, "onDateChangedHIT");
        }
    }

    private void checkDateInStudentRecords() {
        if (classroom != null && date != null) {
            buildFragment();
        }
    }

    private void showDate(int year, int monthOfYear, int dayOfMonth) {
        if (date == null) {
            date = Calendar.getInstance();
        }
        date.set(year, monthOfYear, dayOfMonth);
        dateSelected = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d, yyyy");
        mChooseDate.setText(sdf.format(date.getTime()));
    }


}
