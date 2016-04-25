package com.dyrwi.classroommanager.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.fragments.ClassroomListContentFragment;
import com.dyrwi.classroommanager.fragments.FeedFrag;
import com.dyrwi.classroommanager.fragments.StudentListContentFragment;
import com.dyrwi.classroommanager.repository.Repo;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class NewToolbarMainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private static String APP_NAME = "Classroom Manager";
    private static final String TAG = "NewToolbarMainActivity";

    public static int UPDATE_STUDENT_LIST = 1;
    private Toolbar toolbar;
    private Repo repo;
    private ViewPager viewPager;
    private Drawer drawer;
    private AccountHeader headerResult;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updated_main_activity);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).colorRes(R.color.white));

        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_menu).colorRes(R.color.white).sizeDp(24));
        setSupportActionBar(this.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick for navigation HIT");
                if (drawer != null)
                    drawer.openDrawer();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        setFABforStudentList();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setOnTabSelectedListener(this);

        repo = new Repo(this);

        headerResult = buildDrawerHeader();
        drawer = buildDrawer();
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new FeedFrag(), "Feed");
        adapter.addFragment(new StudentListContentFragment(), "Students");
        adapter.addFragment(new ClassroomListContentFragment(), "Classrooms");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch(tab.getPosition()) {
            case 0:
                setFABforFeed();
                break;
            case 1:
                setFABforStudentList();
                break;
            case 2:
                setFABforClassroomList();
                break;
            default:
                break;

        }
    }

    private void setFABforStudentList() {
        viewPager.setCurrentItem(1);
        if(fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(NewToolbarMainActivity.this, AddEditStudent.class);
                    startActivityForResult(i, UPDATE_STUDENT_LIST);
                }
            });
        }
    }

    private void setFABforClassroomList() {
        viewPager.setCurrentItem(2);
        if(fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add New Classroom.
                }
            });
        }
    }

    private void setFABforFeed() {
        viewPager.setCurrentItem(0);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private AccountHeader buildDrawerHeader() {
        return new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.nav_banner_blank)
                .addProfiles(
                        new ProfileDrawerItem().withName(APP_NAME).withIcon(R.drawable.avatar_blank)
                )
                .build();
    }

    private Drawer buildDrawer() {
        Log.i(TAG, "drawing Drawer");
        return new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.nav_header)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_dashboard)
                                .withName("Feed")
                                .withIdentifier(1)
                                .withTextColor(Color.parseColor("#232A2A")),
                        new PrimaryDrawerItem()
                                .withName("Students")
                                .withIdentifier(2)
                                .withIcon(GoogleMaterial.Icon.gmd_people)
                                .withTextColor(Color.parseColor("#232A2A")),
                        new PrimaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_class)
                                .withName("Classrooms")
                                .withIdentifier(3)
                                .withTextColor(Color.parseColor("#232A2A")),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_add_circle_outline)
                                .withName("Add Classroom Record")
                                .withIdentifier(6)
                                .withTextColor(Color.parseColor("#232A2A")),
                        new SecondaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_person_add)
                                .withName("Add Student")
                                .withIdentifier(7)
                                .withTextColor(Color.parseColor("#232A2A")),
                        new SecondaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_playlist_add)
                                .withName("Add Classroom")
                                .withIdentifier(8)
                                .withTextColor(Color.parseColor("#232A2A")),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_settings)
                                .withName("Settings")
                                .withIdentifier(12)
                                .withTextColor(Color.parseColor("#232A2A")),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_add)
                                .withName("Populate Data")
                                .withIdentifier(14)
                                .withTextColor(Color.parseColor("#232A2A")),
                        new SecondaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_delete)
                                .withName("Delete All Data")
                                .withIdentifier(15)
                                .withTextColor(Color.parseColor("#232A2A")),
                        new SecondaryDrawerItem()
                                .withIcon(GoogleMaterial.Icon.gmd_add)
                                .withName("Delete All Student Records")
                                .withIdentifier(16)
                                .withTextColor(Color.parseColor("#232A2A"))
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        switch (iDrawerItem.getIdentifier()) {
                            case 1: // FEED
                                break;
                            case 2:// STUDENTS
                                break;
                            case 3:// CLASSROOMS
                                break;
                            case 4:// BOOKS
                                break;
                            case 5:// STAFF
                                break;
                            case 6:// ADD CLASSROOM RECORD
                                Intent classroomRecordIntent = new Intent(getApplicationContext(), ClassroomRecordActivity.class);
                                startActivity(classroomRecordIntent);
                                break;
                            case 7:// ADD STUDENT
                                Intent studentIntent = new Intent(getApplicationContext(), AddEditStudent.class);
                                startActivity(studentIntent);
                                break;
                            case 8:// ADD CLASSROOM
                                Intent classroomIntent = new Intent(getApplicationContext(), AddNewClassroom.class);
                                startActivity(classroomIntent);
                                break;
                            case 9:// ADD BOOK
                                break;
                            case 10:// ADD STAFF
                                break;
                            case 11:// ADD HOMEWORK
                                break;
                            case 12:// SETTINGS
                                break;
                            case 13:// HELP
                                break;
                            case 14:// POPULATE DATA
                                repo.factoryReset();
                                Toast.makeText(getApplicationContext(), "Populated data", Toast.LENGTH_SHORT).show();
                                break;
                            case 15:// DELETE ALL DATA
                                repo.deleteAllData();
                                Toast.makeText(getApplicationContext(), "ALL DATA DELETED", Toast.LENGTH_SHORT).show();
                                break;
                            case 16:// DELETE ALL STUDENT RECORDS
                                repo.studentRecords.deleteAll();
                                Toast.makeText(getApplicationContext(), "ALL STUDENT RECORDS DELETED", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                }).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == UPDATE_STUDENT_LIST) {
                sendBroadcast(new Intent("StudentList"));
            }
        }
    }
}
