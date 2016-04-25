/*
package com.dyrwi.classroommanager.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.dyrwi.classroommanager.R;
import com.dyrwi.classroommanager.repository.Repo;
import com.dyrwi.classroommanager.util.MainActivityPagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private Repo repo;
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private Drawer drawer;
    private AccountHeader headerResult;
    private GoogleApiClient mGoogleApiClient;
    private String userName = "userName";
    private String userEmail = "userEmail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        viewPager = (ViewPager) findViewById(R.id.fragment_content);
        viewPager.setAdapter(new MainActivityPagerAdapter(getSupportFragmentManager()));
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        repo = new Repo(this);
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        getSupportActionBar().setTitle("Feed");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Students " + repo.students.getAll().size());
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Classrooms " + repo.classrooms.getAll().size());
                        break;
                    case 3:
                        getSupportActionBar().setTitle("Extra");
                        break;
                    default:
                        getSupportActionBar().setTitle("Classroom Manager");
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private AccountHeader buildDrawerHeader() {
        return new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.nav_banner_blank)
                .addProfiles(
                        new ProfileDrawerItem().withName(userName).withEmail(userEmail).withIcon(R.drawable.avatar_blank)
                )
                .build();
    }

    private Drawer buildDrawer() {
        Log.i(TAG, "drawing Drawer");
        return new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
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
                                Intent studentIntent = new Intent(getApplicationContext(), AddNewStudent.class);
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
                                tabStrip.notifyDataSetChanged();
                                break;
                            case 15:// DELETE ALL DATA
                                repo.deleteAllData();
                                Toast.makeText(getApplicationContext(), "ALL DATA DELETED", Toast.LENGTH_SHORT).show();
                                tabStrip.notifyDataSetChanged();
                                break;
                            case 16:// DELETE ALL STUDENT RECORDS
                                repo.studentRecords.deleteAll();
                                Toast.makeText(getApplicationContext(), "ALL STUDENT RECORDS DELETED", Toast.LENGTH_SHORT).show();
                                tabStrip.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
        tabStrip.notifyDataSetChanged();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected: " + bundle);
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        userName = currentUser.getDisplayName();
        userEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);

        headerResult = buildDrawerHeader();
        drawer = buildDrawer();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
}
*/
