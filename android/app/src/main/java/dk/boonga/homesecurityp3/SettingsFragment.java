package dk.boonga.homesecurityp3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "SettingsFragment";

    FirebaseAuth mAuth;
    FirebaseFirestore dataBase;


    Button mbuttonSignOut;
    Button mbuttonChangePassword;
    Button mButtonDeleteAccount;
    EditText mEditTextChangePassword;
    EditText mEditTextMatchChangePassword;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        dataBase = FirebaseFirestore.getInstance();
        mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mbuttonSignOut = view.findViewById(R.id.buttonSignOutId);
        mbuttonChangePassword = view.findViewById(R.id.buttonChangePasswordId);
        mEditTextChangePassword = view.findViewById(R.id.editTextChangePasswordId);
        mEditTextMatchChangePassword = view.findViewById(R.id.editTextMatchChangePasswordId);
        mButtonDeleteAccount = view.findViewById(R.id.buttonDeleteAccountId);
        mbuttonSignOut.setOnClickListener(this);
        mbuttonChangePassword.setOnClickListener(this);
        mButtonDeleteAccount.setOnClickListener(this);
        return view;
    }

    public void updatePassword() {
        String password = mEditTextChangePassword.getText().toString().trim();
        String confirmPassword = mEditTextMatchChangePassword.getText().toString().trim();
        if (password.isEmpty()) {
            mEditTextChangePassword.setError("Password is required");
            mEditTextChangePassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mEditTextChangePassword.setError("Minimum length of password should be 6");
            mEditTextChangePassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            mEditTextMatchChangePassword.setError(getString(Integer.parseInt("Passwords do not match")));
            mEditTextMatchChangePassword.requestFocus();
        }

        mAuth.getCurrentUser().updatePassword(password);
    }



    @Override
    public void onResume() {
        super.onResume();


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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //When Sign up button is pressed, call the method registerUser
            case R.id.buttonSignOutId:
                Log.d(TAG, "User signed out");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);

                mAuth.signOut();
                break;


            case R.id.buttonChangePasswordId:
                Log.d(TAG, "change password button clicked");
                updatePassword();
                Toast.makeText(getContext(), "Your password has been changed", Toast.LENGTH_SHORT).show();
                break;


            case R.id.buttonDeleteAccountId:
                Log.d(TAG, "Delete Account clicked");
                    mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {

                    Log.d(TAG, "LOGGED IN");

                    FirebaseUser fireUser = mAuth.getCurrentUser();
                    assert fireUser != null;
                    final String UID = fireUser.getUid();
                    Log.d(TAG, "msg 1: "+ dataBase.collection("users").document(UID));
                    Log.d(TAG, "msg 2: " + fireUser.getUid());
                    dataBase.collection("users").document(UID).delete();

                            mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Log.d(TAG, "User account deleted.");
                                        Toast.makeText(getContext(),
                                                "Your account has been deleted and you have been logged out.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                   else {

                                       Log.d(TAG, "Failed");
                                    }

                                }
                            });

                }
                break;
        }
    }
}