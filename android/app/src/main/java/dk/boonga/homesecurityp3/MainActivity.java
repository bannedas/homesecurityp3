package dk.boonga.homesecurityp3;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements fragment_all_rooms.OnFragmentInteractionListener,
                fragment_sensor.OnFragmentInteractionListener, fragment_front_page.OnFragmentInteractionListener,
                fragment_sensors.OnFragmentInteractionListener, fragment_settings.OnFragmentInteractionListener{
    private static final String TAG = "MainScreenActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    fragment = new fragment_front_page();
                    loadFragment(fragment, R.id.fragment_container_main);
                    return true;
                case R.id.navigation_rooms:
                    fragment = new fragment_all_rooms();
                    loadFragment(fragment, R.id.fragment_container_main);
                    return true;
                case R.id.navigation_settings:
                    fragment = new fragment_settings();
                    loadFragment(fragment,R.id.fragment_container_main);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment, int fragmentContainerID) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainerID, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize bottom navigation bar
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Initialize fragments in toolbar frame and main frame
        loadFragment(new fragment_front_page(),R.id.fragment_container_main);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}