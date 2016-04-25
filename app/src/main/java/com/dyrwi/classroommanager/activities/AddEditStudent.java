package com.dyrwi.classroommanager.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.model.Classroom;
import com.dyrwi.classroommanager.model.Student;
import com.dyrwi.classroommanager.repository.Repo;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ben on 02-Oct-15.
 */
public class AddEditStudent extends AppCompatActivity {
    private static final String TAG = "AddEditStudent";
    public static final String SELECTED_IMAGE = "selected_image";
    public static String STUDENT_ID = "student_id";

    //onActivityResult
    public static int IMAGE_FROM_CROP = 1;

    // onSavedInstanceStatus
    static final String IMAGE_URI = "imageURI";
    static final String ENGLISH_NAME = "englishName";
    static final String KOREAN_NAME = "koreanName";
    static final String PHONE_NUMBER = "phoneNumber";
    static final String DATE_OF_BIRTH = "dateOfBirth";
    static final String CLASSROOM = "classroom";
    static final String MESSAGE = "message";

    private Repo repo;

    // Widgets
    private ImageView mImage;
    private EditText mEnglishName;
    private EditText mKoreanName;
    private EditText mPhoneNumber;
    private EditText mMessage;
    private Button mDateOfBirth;
    private Spinner mClassroomSpinner;
    private Toolbar toolbar;
    private android.support.design.widget.FloatingActionButton mFab;

    // Java
    private Uri imageUri;
    private String imageURI = "";
    private Calendar dateOfBirth = Calendar.getInstance();
    private String englishName = "";
    private String koreanName = "";
    private String phoneNumber = "";
    private String message = "";
    private Classroom classroom = new Classroom();
    private Student student;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        repo = new Repo(this);
        setContentView(R.layout.add_new_student);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back).colorRes(R.color.white).sizeDp(16));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Student");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!changesMade())
                    onBackPressed();
                else {
                    new AlertDialog.Builder(AddEditStudent.this)
                            .setTitle("Not saved")
                            .setMessage("Are you sure you want to discard this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        setWidgets();
        if (savedInstanceStatus != null) {
            mImage.setImageURI(Uri.parse(savedInstanceStatus.getString(IMAGE_URI)));
            mEnglishName.setText(savedInstanceStatus.getString(ENGLISH_NAME));
            mKoreanName.setText(savedInstanceStatus.getString(KOREAN_NAME));
            mPhoneNumber.setText(savedInstanceStatus.getString(PHONE_NUMBER));
            if (savedInstanceStatus.getLong(DATE_OF_BIRTH) != 0) {
                dateOfBirth = Calendar.getInstance();
                dateOfBirth.setTimeInMillis(savedInstanceStatus.getLong(DATE_OF_BIRTH));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                mDateOfBirth.setText(sdf.format(dateOfBirth.getTime()));
            } else {
                mDateOfBirth.setText("CHOOSE DATE OF BIRTH");
            }
            mMessage.setText(savedInstanceStatus.getString(MESSAGE));
        }

        /**
         * Load student from known profile via intent.
         */
        student = new Student();
        if (getIntent() != null) {
            try {
                student = repo.students.findByID(getIntent().getLongExtra(STUDENT_ID, 0));
                Log.d(TAG, "Student: " + student.getEnglishName());
                Picasso.with(this).load(Uri.parse(student.getPictureUri())).error(R.drawable.avatar_blank).fit().centerCrop().into(mImage);
                mEnglishName.setText(student.getEnglishName());
                mKoreanName.setText(student.getKoreanName());
                mPhoneNumber.setText(student.getPhoneNumber());
                dateOfBirth = Calendar.getInstance();
                dateOfBirth.setTimeInMillis(student.getDateOfBirth().getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                mDateOfBirth.setText(sdf.format(dateOfBirth.getTime()));
                mMessage.setText(student.getMessage());
                getSupportActionBar().setTitle("Edit Student: " + student.getKoreanAndEnglishNames());
            } catch (NullPointerException ex) {
                Log.d(TAG, "New student being created");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(IMAGE_URI, imageURI);
        savedInstanceState.putString(ENGLISH_NAME, mEnglishName.getText().toString());
        savedInstanceState.putString(KOREAN_NAME, mKoreanName.getText().toString());
        savedInstanceState.putString(PHONE_NUMBER, mPhoneNumber.getText().toString());
        savedInstanceState.putLong(DATE_OF_BIRTH, dateOfBirth.getTimeInMillis());
        savedInstanceState.putString(MESSAGE, mMessage.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    private boolean changesMade() {
        if ((mEnglishName.getText().toString().equalsIgnoreCase("")) &&
                (mKoreanName.getText().toString().equalsIgnoreCase("")) &&
                (mPhoneNumber.getText().toString().equalsIgnoreCase("")) &&
                (mMessage.getText().toString().equalsIgnoreCase(""))) {
            return false;
        } else
            return true;
    }

    private void setWidgets() {
        mImage = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(R.drawable.avatar_blank).fit().centerCrop().into(mImage);
        ImageView mEnglishNameIcon = (ImageView) findViewById(R.id.english_name_icon);
        mEnglishNameIcon.setImageDrawable(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_alphabetical).colorRes(R.color.colorAccent).sizeDp(24));
        mEnglishName = (EditText) findViewById(R.id.english_name_data);
        mEnglishName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                englishName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ImageView mKoreanNameIcon = (ImageView) findViewById(R.id.korean_name_icon);
        mKoreanNameIcon.setImageDrawable(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_hand_pointing_right).colorRes(R.color.colorAccent).sizeDp(24));
        mKoreanName = (EditText) findViewById(R.id.korean_name_data);
        mKoreanName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                koreanName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ImageView mPhoneNumberIcon = (ImageView) findViewById(R.id.phone_icon);
        mPhoneNumberIcon.setImageDrawable(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_phone).colorRes(R.color.colorAccent).sizeDp(24));
        mPhoneNumber = (EditText) findViewById(R.id.phone_data);
        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ImageView mMessageIcon = (ImageView) findViewById(R.id.message_icon);
        mMessageIcon.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_add).colorRes(R.color.colorAccent).sizeDp(24));
        mMessage = (EditText) findViewById(R.id.message_data);
        mMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                message = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ImageView mDateOfBirthIcon = (ImageView) findViewById(R.id.date_of_birth_icon);
        mDateOfBirthIcon.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_cake).colorRes(R.color.colorAccent).sizeDp(24));
        mDateOfBirth = (Button) findViewById(R.id.date_of_birth_data);
        mDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment dateFragment = new DateDialogFragment();
                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });
        ImageView mClassroomSpinnerIcon = (ImageView) findViewById(R.id.classroom_icon);
        mClassroomSpinnerIcon.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_class).colorRes(R.color.colorAccent).sizeDp(24));
        mClassroomSpinner = (Spinner) findViewById(R.id.classroom_data);
        ArrayList<Classroom> classroomList = (ArrayList<Classroom>) repo.classrooms.getAll();
        mClassroomSpinner.setAdapter(new ClassroomListAdapter(this, classroomList));
        mClassroomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.i(TAG, parent.getItemAtPosition(position).toString());
                classroom = (Classroom) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_camera).colorRes(R.color.white).sizeDp(32));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getApplicationContext(), CropImage.class);
                //startActivityForResult(intent, IMAGE_FROM_CROP);
                startActivityForResult(getPickImageChooserIntent(), IMAGE_FROM_CROP);*/
                startActivityForResult(pictureIntentChooser(), IMAGE_FROM_CROP);
            }
        });
    }

    public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        public DateDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(year, monthOfYear, dayOfMonth);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.confirm:
                createOrUpdateStudent();
                return true;
            default:
                return true;
        }
    }

    private void createOrUpdateStudent() {
        if (student == null)
            student = new Student();
        student.setKoreanName(koreanName);
        if (imageUri != null)
            student.setPictureUri(imageUri.toString());
        Log.i(TAG, student.getPictureUri());
        student.setEnglishName(englishName);
        student.setPhoneNumber(phoneNumber);
        if (student.getDateCreated() == null) {
            student.setDateCreated(Calendar.getInstance().getTime());
        }
        student.setDateModified(Calendar.getInstance().getTime());
        student.setDateOfBirth(dateOfBirth.getTime());
        student.setMessage(message);
        student.setClassroom(classroom);
        Log.i(TAG, student.toString());

        repo.students.createOrUpdate(student);
        if (classroom != null) {
            classroom.setName(classroom.getEnglishNamesOfStudents());
            repo.classrooms.update(classroom);
        }
        repo.students.update(student);

        setResult(Activity.RESULT_OK);
        finish();
    }

    private void showDate(int year, int monthOfYear, int dayOfMonth) {
        if (dateOfBirth == null) {
            dateOfBirth = Calendar.getInstance();
        }
        dateOfBirth.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy, MM, dd");
        mDateOfBirth.setText(sdf.format(dateOfBirth.getTime()));
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
            View v = convertView;
            ClassroomListViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.fragment_advanced_classroom_list, null);
                viewHolder = new ClassroomListViewHolder(v);
                v.setTag(viewHolder);
            } else {
                viewHolder = (ClassroomListViewHolder) v.getTag();
            }
            Picasso.with(getApplicationContext()).load(Uri.parse(mList.get(position).getPictureUri())).error(R.drawable.avatar_blank).placeholder(R.drawable.avatar_blank).fit().centerCrop().into(viewHolder.mImage);
            viewHolder.mName.setText(mList.get(position).getName());

            StringBuilder koreanNames = new StringBuilder();
            try {

                ForeignCollection<Student> students = mList.get(position).getStudents();
                CloseableIterator<Student> iterator = students.closeableIterator();
                while (iterator.hasNext()) {
                    Student s = iterator.next();
                    koreanNames.append(s.getKoreanName() + " ");
                }
                iterator.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NullPointerException ex) {
                Log.i(TAG, "No students found");
                koreanNames.append("No students found");
            }
            viewHolder.mKoreanNames.setText(koreanNames.toString());
            return v;
        }
    }

    private class ClassroomListViewHolder {
        public ImageView mImage;
        public TextView mName;
        public TextView mKoreanNames;

        public ClassroomListViewHolder(View base) {
            mImage = (ImageView) base.findViewById(R.id.fragment_advanced_classroom_list_image);
            mName = (TextView) base.findViewById(R.id.fragment_advanced_classroom_list_name);
            mKoreanNames = (TextView) base.findViewById(R.id.fragment_advanced_classroom_list_student_korean_names);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_FROM_CROP) {
                imageUri = getPickImageResultUri(data);
                Picasso.with(this).load(imageUri).fit().centerCrop().into(mImage);
            }
        }
    }

    public Intent pictureIntentChooser() {
        // Determine Uri of camera image to  save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            if (!allIntents.contains(intent)) {
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);
                if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                }
                allIntents.add(intent);
            }
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select Source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to  save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            if (!allIntents.contains(intent)) {
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);
                if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                }
                allIntents.add(intent);
            }
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select Source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture  by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from  {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

}
