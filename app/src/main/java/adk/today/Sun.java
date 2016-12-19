package adk.today;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class Sun extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int reset_check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* The creation phases are almost the same as in Home.class .*/

        SharedPreferences sharedPref = getSharedPreferences("ThemeData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int tid = sharedPref.getInt("ThemeID", -1);

        if (tid == -1) {
            tid = 0;
            editor.putInt("ThemeID", 0);
            editor.commit();
        }
        if (tid == 0) {
            setTheme(R.style.SunriseRed);
        } else if (tid == 1) {
            setTheme(R.style.BlueHaze);
        } else if (tid == 2) {
            setTheme(R.style.WoodPresence);
        } else {
            setTheme(R.style.Classic);
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.Set);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        View navView = navigationView.getHeaderView(0);
        ((TextView) navView.findViewById(R.id.sun_name)).setText(sharedPreferences.getString("UserName", "User"));

        // Initialise , animate and handle the events on the three Floating Action Buttons
        // in the layout.

        Animation rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hover);
        FloatingActionButton f_theme = ((FloatingActionButton) findViewById(R.id.theme));
        FloatingActionButton f_reset = ((FloatingActionButton) findViewById(R.id.reset));
        FloatingActionButton f_user = ((FloatingActionButton) findViewById(R.id.user));

        f_theme.startAnimation(rotate_forward);
        f_reset.startAnimation(rotate_forward);
        f_user.startAnimation(rotate_forward);

        f_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_check = 0;

                SharedPreferences sharedPref = getSharedPreferences("ThemeData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("ThemeID", (sharedPref.getInt("ThemeID", -1) + 1) % 4);
                Log.d("THEMER", "Theme set to " + sharedPref.getInt("ThemeID", -1));
                editor.commit();

                finish();
                startActivity(new Intent(getApplicationContext(), Sun.class));


            }
        });

        f_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reset_check++ == 0) {
                    Toast.makeText(getApplicationContext(), "Press again to confirm reset .", Toast.LENGTH_SHORT).show();
                } else {
                    Storage s = new Storage(getBaseContext());
                    s.reset();
                    Toast.makeText(getApplicationContext(), "Reset Complete .", Toast.LENGTH_SHORT).show();
                    reset_check = 0;
                }
            }
        });

        f_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_check = 0;

                UserNameUpdation userNameUpdation = new UserNameUpdation();
                userNameUpdation.show(getFragmentManager(), "UND");

                //finish();
                //startActivity(new Intent(getApplicationContext(), Sun.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();
        String day = "Monday";

        if (id == R.id.Monday) {
            day = "Monday";
        } else if (id == R.id.Tuesday) {
            day = "Tuesday";
        } else if (id == R.id.Wednesday) {
            day = "Wednesday";
        } else if (id == R.id.Thursday) {
            day = "Thursday";
        } else if (id == R.id.Friday) {
            day = "Friday";
        } else if (id == R.id.Saturday) {
            day = "Saturday";
        } else if (id == R.id.About) {
            Toast.makeText(this, "Developed by ADK", Toast.LENGTH_LONG).show();
        }

        if (id != R.id.Set && id != R.id.About) {
            drawer.closeDrawer(GravityCompat.START);
            Intent i = new Intent(this, Home.class);
            i.putExtra("Day", day);
            finish();
            startActivity(i);
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
