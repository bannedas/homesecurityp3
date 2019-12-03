package dk.boonga.homesecurityp3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.text.InputType.TYPE_CLASS_TEXT;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrontPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrontPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrontPageFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "FrontPageFragment";
    String UID;
    public String pinDocument;
    public EditText mEditTextPin;

    private FirebaseAuth mAuth;
    //init firestore database sdk
    FirebaseFirestore db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public FrontPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrontPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FrontPageFragment newInstance(String param1, String param2) {
        FrontPageFragment fragment = new FrontPageFragment();
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
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_front_page, container, false);

        ToggleButton btn = v.findViewById(R.id.toggleButtonOnOffId);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpEditText();
            }
        };
        btn.setOnClickListener(listener);


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
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.toggleButtonOnOffId:
                Log.d(TAG, "Button pressed");
                popUpEditText();

                break;
        }
    }


    public void popUpEditText() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        mEditTextPin = new EditText(getContext());
        mEditTextPin.setInputType(TYPE_CLASS_TEXT);
        alert.setMessage("Enter your PIN to turn ON/OFF the system");
        alert.setTitle("PIN");

        alert.setView(mEditTextPin);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    mAuth.signOut();
                }
                else{
                    UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    db.collection("users").document(UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                 pinDocument = task.getResult().getString("PIN");
                                assert pinDocument != null;
                                Log.d(TAG, "" + pinDocument + " " + mEditTextPin.getText());


                                if (Objects.equals(mEditTextPin.getText().toString(), pinDocument)) {
                                    Toast.makeText(getContext(), "System turned ON/OFF", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getContext(), "PIN is wrong", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, mEditTextPin.getText().toString() + " " + pinDocument);
                                }
                            }
                        }});
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();


    }
}


