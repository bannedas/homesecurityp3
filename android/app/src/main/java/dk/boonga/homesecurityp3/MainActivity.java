package dk.boonga.homesecurityp3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements fragment_all_rooms.OnFragmentInteractionListener,
                fragment_sensor.OnFragmentInteractionListener, fragment_front_page.OnFragmentInteractionListener,
                fragment_sensors.OnFragmentInteractionListener, fragment_settings.OnFragmentInteractionListener{
    private static final String TAG = "MainScreenActivity";

    private final static String APP_PACKAGE = "dk.boonga.homesecurityp3";
    private final static String SENSOR_CHANEL_ID = APP_PACKAGE + ".SENSOR_CHANNEL";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize bottom navigation bar
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Initialize fragments in toolbar frame and main frame
        loadFragment(new fragment_front_page(),R.id.fragment_container_main);

        // ---------------- notifications ----------------------

        // tutorial https://developer.android.com/training/notify-user/build-notification
        // create notification channel / required from android 8.0 +
        createNotificationChannel();

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("motion-sensor");

    }

    @Override
    protected void onStart() {

        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean connected = dataSnapshot.getValue(Boolean.class);

                if(connected) {
                    popUpNotification("ALERT", "Motion sensor detected movement");
                } else {
                    popUpNotification("CALM DOWN", "Movement stopped");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Database error", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel-name";
            String description = "Channel-description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(SENSOR_CHANEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    private void popUpNotification(String title, String desc) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(getApplicationContext(), SENSOR_CHANEL_ID)
                .setSmallIcon(R.drawable.button_power)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder2.build());
    }
}