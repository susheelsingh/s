package com.qrms.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.qrms.Common.ShareData;
import com.qrms.Fragment.ExpenseCategoryFragment;
import com.qrms.Fragment.InternalStaffFragment;
import com.qrms.Fragment.InternalStaffListingFragment;
import com.qrms.Fragment.ProfileFragment;
import com.qrms.Fragment.TimeSheetFragment;
import com.qrms.Fragment.TsetFragment;
import com.qrms.Fragment.VisitorDashboardFragment;
import com.qrms.Fragment.VisitorListFragment;
import com.qrms.Fragment.VisitorMangementFragment;
import com.qrms.Fragment.VisitorSecurityMangementFragment;
import com.qrms.R;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    public final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View headerView;
    private ImageView Logo;
    private String VisitorManagesLogged;
    /*private TextView Name, Designation;*/
    private Menu menu;
    private ShareData sharedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        DeclareVariables();

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(DashboardActivity.this);
        /*Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.nav_current_data, false);
            }
        });*/

        displaySelectedScreen(R.id.Nav_VisitorDashboard, true);


    }
    public void DeclareVariables() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView =  navigationView.getHeaderView(0);
        Logo = headerView.findViewById(R.id.logo);
        menu = navigationView.getMenu();
        sharedData=new ShareData(DashboardActivity.this);
        VisitorManagesLogged= sharedData.getString("logged_userId", "");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
            if (fragment != null) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
                super.onBackPressed();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                } else {
                    finish();
                }
                System.exit(0);
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId(), false);
        return true;
    }

    private void displaySelectedScreen(int itemId, boolean first) {

        Fragment fragment = null;
        switch (itemId) {
            case R.id.Nav_VisitorDashboard:
                    fragment = new VisitorDashboardFragment();
                break;
            case R.id.nav_Visitor:
                fragment = new VisitorMangementFragment();
                break;
            case R.id.nav_VisitorList:
                fragment= new VisitorListFragment();
                break;

            case R.id.nav_InternalStaff:
                fragment= new InternalStaffFragment();
               // fragment=new TsetFragment();
                break;
            case R.id.nav_InternalStaffList:
                fragment= new InternalStaffListingFragment();
                break;
            case R.id.nav_AttendenceReport:
                fragment= new TimeSheetFragment();
                break;
            case R.id.nav_MonthlyAttendenceReport:
                fragment= new TimeSheetFragment();
                break;
            case R.id.nav_LogOut:
                MenuItem nav_logout = menu.findItem(R.id.nav_LogOut);
                sharedData.getBoolean("is_user_logged_in",false);
                sharedData.clearAll();
                startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
                finish();
                Toast.makeText(DashboardActivity.this,"User is suscessfully logout !!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_TodaysTimeOut:
                fragment= new InternalStaffFragment();
                break;
            case R.id.nav_AdminProfile:
                fragment=new ProfileFragment();
                break;
        }
        if (fragment != null) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (currentFragment == null || currentFragment.getClass() != fragment.getClass()) {
                OpenFragment(fragment, first);
            } else {
                CloseDraver();
            }
        } else {
            CloseDraver();
        }

    }

    private void OpenFragment(Fragment fragment, boolean first) {
        if (fragment != null) {
            //replacing the fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (first) {
                ft.add(R.id.content_frame, fragment);
            } else {
                ft.replace(R.id.content_frame, fragment, TAG_FRAGMENT);
            }
            ft.addToBackStack(null);
            ft.commit();
        }
        CloseDraver();
    }
    private void CloseDraver () {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


}