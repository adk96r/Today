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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public String day;
    private RecyclerView classesRecyclerView;
    private RecyclerView.Adapter classesAdapter;
    private Animation rotate_forward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the app is being run for the first time //

        SharedPreferences s = getSharedPreferences("initData", Context.MODE_PRIVATE);
        if (s.getInt("first", 1) == 1) {
            new Storage(getApplicationContext()).setUpInit();
            SharedPreferences.Editor editor = s.edit();
            editor.putInt("first", 0);
            editor.commit();
        }

        /*
         * There can be two situations :
         *
         *      1 > Home.class is run by default . So simply display the respective day . ( Display
         *          Monday if the day is Sunday )
         *
         *      2 > Home.class is called by the navigation drawer of Sun.class.
         *          In such case the day selected is passed along with the intent , as a parameter
         *          under the name "Day" . Thus , we are extracting it and displaying the
         *          respective day.
         */

        Intent i = this.getIntent();

        if (i.getExtras() != null && i.getExtras().containsKey("Day")) {
            this.day = i.getStringExtra("Day");
            setupDay(i.getStringExtra("Day"));
        } else if (!Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).equals("Sunday")) {
            String dayLongName = Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
            this.day = dayLongName;
            setupDay(this.day);
        } else {
            this.day = "Monday";
            setupDay(this.day);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        int id = item.getItemId();

        if (id == R.id.Monday) {
            this.day = "Monday";
        } else if (id == R.id.Tuesday) {
            this.day = "Tuesday";
        } else if (id == R.id.Wednesday) {
            this.day = "Wednesday";
        } else if (id == R.id.Thursday) {
            this.day = "Thursday";
        } else if (id == R.id.Friday) {
            this.day = "Friday";
        } else if (id == R.id.Saturday) {
            this.day = "Saturday";
        } else if (id == R.id.Set) {
            finish();
            startActivity(new Intent(this, Sun.class));
        } else if (id == R.id.About) {
            Toast.makeText(this, "Developed by ADK", Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(day);

        /*
         *  Everytime a new day is selected in the navigation drawer , the Floating Action Button
         *  has to be recreated since the day is changing. There is just one global parameter "day"
         *  which is changed when the new day is selected .
         *
         *  After setting it up simply refresh the adapter for the day pass this new adapter to
         *  the RecyclerView.
         */

        try {
            setupFAB(this.day);
            classesAdapter = new PeriodCardAdapter(getBaseContext(), this.day, getFragmentManager(), classesRecyclerView);
            classesRecyclerView.setAdapter(classesAdapter);
        } catch (Exception e) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void setupDay(String day) {

        /*
        *   Setup will create the screen dynamically depending on the day.
        *
        *   Following are the chronological phases which take place :
        *
        *       1) Set the User's theme . ( If no theme has been set yet , Select Theme 0 .)
        *
        *       2) Set up the following:
        *                i) layout activity._home
        *               ii) toolbar and the the day displayed on the toolbar
        *              iii) Floating Action Button - SetupFAB(day) basically does this and makes
        *                   sure that if a new period is added it is saved with correct data (Day).
        *               iv) Navidation Drawer and the User's Name inside the drawer.
        *       3) Get all data for the day. Period Card Adapter returns a RecylerView.Adapter.
        *
        *       4) Initiate the RecyclerView and provide it the Adapter just created.
        *
        */

        // Phase 1 /////////////////////////////////////////////////////////////////////////////////

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

        // Phase 2 /////////////////////////////////////////////////////////////////////////////////

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(day);
        setSupportActionBar(toolbar);

        setupFAB(day);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(resolveDay(day));

        // This sets the name of the user in the Navigation Drawer's Header. //
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        View navView = navigationView.getHeaderView(0);
        ((TextView) navView.findViewById(R.id.home_name))
                .setText(sharedPreferences.getString("UserName", "User"));


        // Phase 3 /////////////////////////////////////////////////////////////////////////////////
        classesAdapter = new PeriodCardAdapter(getBaseContext(), day, getFragmentManager(),
                classesRecyclerView);

        // Phase 4 /////////////////////////////////////////////////////////////////////////////////
        classesRecyclerView = (RecyclerView) findViewById(R.id.classes);
        RecyclerView.LayoutManager classesLayoutManager = new LinearLayoutManager(this);
        classesRecyclerView.setLayoutManager(classesLayoutManager);
        classesRecyclerView.setAdapter(classesAdapter);

    }

    public int resolveDay(String day) {

        switch (day) {
            case "Monday":
                return R.id.Monday;
            case "Tuesday":
                return R.id.Tuesday;
            case "Wednesday":
                return R.id.Wednesday;
            case "Thursday":
                return R.id.Thursday;
            case "Friday":
                return R.id.Friday;
            default:
                return R.id.Saturday;
        }
    }

    public void setupFAB(final String day) {

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.startAnimation(rotate_forward);
                NewClass newClass = new NewClass();
                newClass.setDay(day);
                newClass.initFAB(fab);
                newClass.initAdapter(classesAdapter);
                newClass.show(getFragmentManager(), "Add a new class dialog.");

            }
        });
    }

}

