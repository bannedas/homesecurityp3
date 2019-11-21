package dk.boonga.homesecurityp3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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


public class AllRoomsFragment extends Fragment {

    private static final String TAG = "AllRoomsFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recycle_view_room;
    private List<Room> mListRoom = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String UID;

    private String mRoomID = "";

    public AllRoomsFragment() {
        // Required empty public constructor
    }

    public static AllRoomsFragment newInstance(String param1, String param2) {
        AllRoomsFragment fragment = new AllRoomsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // init firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Get User ID
        FirebaseUser fireUser = mAuth.getCurrentUser(); //get user info
        assert fireUser != null;
        UID = fireUser.getUid(); //store user id

//        manageRoom(UID, "A", true);
//        manageRoom(UID, "B", true);
//        manageRoom(UID, "C", true);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_rooms, container, false);
        recycle_view_room = v.findViewById(R.id.recycle_view_room_id);

        updateRoomList(); // load all user rooms from firebase

        /** Add rooms dynamically */
        FloatingActionButton btn = v.findViewById(R.id.recycle_view_add_room);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Room");

                // Set up the mInput
                final EditText input = new EditText(getContext());
                // Specify the type of mInput expected; this, for example, sets the mInput as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRoomID = input.getText().toString();
                        addRoom(UID, mRoomID);
                        updateRoomList();
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
        /** end rooms */

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

    private void updateRoomList() {
        DocumentReference docRef = db.collection("user-Room")
                .document(UID)
                .collection("rooms")
                .document("Room-list");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                mListRoom = new ArrayList<>();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            mListRoom.add(new Room(entry.getKey(), "photo-url-goes-here"));
                        }
                    }
                    AdapterRooms mAdapterRooms = new AdapterRooms(getContext(), mListRoom);
                    recycle_view_room.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    recycle_view_room.setAdapter(mAdapterRooms);
                }
            }
        });
    }

    private void addRoom(String UID, String mRoomID) {
        // Update one field, creating the document if it does not already exist.
        Map<String, Boolean> data = new HashMap<>();
        data.put(mRoomID, true);

        DocumentReference docRef =
                db.collection("user-Room")          // first folder user-Room
                        .document(UID)                           // second folder userID
                        .collection("rooms")  // third folder Room-id
                        .document("Room-list");
        docRef.set(data, SetOptions.merge());
    }
}
