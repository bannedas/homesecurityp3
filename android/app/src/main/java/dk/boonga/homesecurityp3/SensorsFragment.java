package dk.boonga.homesecurityp3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SensorsFragment extends Fragment {

    private static final String TAG = "SensorsFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recycle_view_sensor;
    private List<Sensor> mListSensor = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String mUserID = "";
    private String mSensorID = "";
    private String mRoomID = "";

    private final static String APP_PACKAGE = "dk.boonga.homesecurityp3";
    private final static String SENSOR_CHANEL_ID = APP_PACKAGE + ".SENSOR_CHANNEL";

    public SensorsFragment() {
        // Required empty public constructor
    }

    public static SensorsFragment newInstance(String param1, String param2) {
        SensorsFragment fragment = new SensorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get roomId from fragment
        Bundle b = getArguments();
        mRoomID = b.getString("roomID");

        // init firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Get User ID
        FirebaseUser fireUser = mAuth.getCurrentUser(); //get user info
        assert fireUser != null;
        mUserID = fireUser.getUid(); //store user id

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sensors, container, false);
        recycle_view_sensor = v.findViewById(R.id.recycle_view_sensors);

        updateSensorList();

        /** Add sensors dynamically */
        FloatingActionButton btn = v.findViewById(R.id.recycle_view_add_sensor);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Sensor");

                // Set up the mInput
                final EditText input = new EditText(getContext());
                // Specify the type of mInput expected; this, for example, sets the mInput as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSensorID = input.getText().toString();
                        addSensor(mUserID, mSensorID, mRoomID);
                        updateSensorList();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        };
        btn.setOnClickListener(listener);
        /** end sensors */


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void updateSensorList() {
        DocumentReference docRef = db.collection("user-Room")
                .document(mUserID)
                .collection("rooms")
                .document("Room-list")
                .collection(mRoomID)
                .document("Sensor-list");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                mListSensor = new ArrayList<>();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            mListSensor.add(new Sensor(entry.getKey(), "photo-url-goes-here"));
                        }
                    }

                    AdapterSensors mAdapterSensors = new AdapterSensors(getContext(), mListSensor);
                    recycle_view_sensor.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
                    recycle_view_sensor.setAdapter(mAdapterSensors);
                }
            }
        });
    }

    private void addSensor(String UID, String sensorID, String roomID) {
        // Update one field, creating the document if it does not already exist.
        Map<String, Boolean> data = new HashMap<>();
        data.put(sensorID, true);

        DocumentReference docRef =
                db.collection("user-Room")          // first folder user-Room
                        .document(UID)                           // second folder userID
                        .collection("rooms")  // third folder Room-id
                        .document("Room-list")
                        .collection(roomID)
                        .document("Sensor-list");

        docRef.set(data, SetOptions.merge());
    }
}