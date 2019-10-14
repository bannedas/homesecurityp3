package dk.boonga.homesecurityp3;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainScreenActivity";

    List<room> mListRoom;

    RecyclerView recyclerViewRooms;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    updateRoomList();
                    return true;
                case R.id.navigation_settings:
                    //mTextMessage.setText(R.string.title_settings);
                    return true;
                case R.id.navigation_profile:
                    //mTextMessage.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }
    };

    private void updateRoomList() {
        mListRoom = new ArrayList<>();

        for(int i = 0; i < 16; i++) {
            mListRoom.add(new room("Room " + i, "test", "test", "test"));
        }

        AdapterRooms mAdapterRooms = new AdapterRooms(this, mListRoom);
        recyclerViewRooms.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewRooms.setAdapter(mAdapterRooms);
        //ViewCompat.setNestedScrollingEnabled(recyclerViewRooms, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerViewRooms = findViewById(R.id.recycle_view_room);
    }

}
