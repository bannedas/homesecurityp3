package dk.boonga.homesecurityp3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class fragment_all_rooms extends Fragment {

    private static final String TAG = "fragment_all_rooms";
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recycle_view_room;
    //Test of recycleview
    private List<room> mListRoom = new ArrayList<>();

    public fragment_all_rooms() {
        // Required empty public constructor
    }

    public static fragment_all_rooms newInstance(String param1, String param2) {
        fragment_all_rooms fragment = new fragment_all_rooms();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_rooms, container, false);
        recycle_view_room = v.findViewById(R.id.recycle_view_room_id);

        final String[] arr={"Living", "Bedroom", "Bathroom", "Kitchen", "Class"};
        final Random r = new Random();

        for(int i = 0; i < 3; i++) {
            int randomNumber = r.nextInt(arr.length);
            mListRoom.add(new room(arr[randomNumber], "test", "test", "test"));
        }

        final AdapterRooms mAdapterRooms = new AdapterRooms(getContext(), mListRoom);

        /** Add rooms dynamically */
        FloatingActionButton btn = v.findViewById(R.id.recycle_view_add_room);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomNumber = r.nextInt(arr.length);
                mListRoom.add(new room(arr[randomNumber], "test", "test", "test"));
                mAdapterRooms.notifyDataSetChanged();

            }
        };
        btn.setOnClickListener(listener);
        /** end rooms */

        recycle_view_room.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycle_view_room.setAdapter(mAdapterRooms);
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
}
