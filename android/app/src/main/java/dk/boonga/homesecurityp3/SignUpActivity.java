package dk.boonga.homesecurityp3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";
    EditText mSignUpEditTextEmail, mSignUpEditTextPassword, mSignUpEditTextConfirmPassword, mSignUpEditFirstName, mSignUpEditLastName, mSignUpEditTextPin;
    Button buttonLogin;
    //ProgressBar signUpProgressBar;

    //This is the entry point for the Firebase Authentication SDK
    private FirebaseAuth mAuth;
    //init firestore database sdk
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        //assign db to our instance
        db = FirebaseFirestore.getInstance();

        mSignUpEditTextEmail = findViewById(R.id.editTextViewSignUpEmailId);
        mSignUpEditTextPassword = findViewById(R.id.editTextViewSignUpPasswordId);
        mSignUpEditTextConfirmPassword = findViewById(R.id.editTextViewSignUpPasswordConfirmId);
        mSignUpEditFirstName = findViewById(R.id.editTextViewSignUpFirstNameId);
        mSignUpEditLastName = findViewById(R.id.editTextViewSignUpLastNameId);
        mSignUpEditTextPin = findViewById(R.id.editTextViewSignUpPinId);

        buttonLogin = findViewById(R.id.buttonSignUpLoginId);
        buttonLogin.setOnClickListener(this);
    }

    /*
    This method Registers the user.
     */
    private void registerUser() {
        /*
        This gets the text written by the user in the EditTextFields email and password.
         */
        String email = mSignUpEditTextEmail.getText().toString().trim();
        String password = mSignUpEditTextPassword.getText().toString().trim();
        final String firstName = mSignUpEditFirstName.getText().toString().trim();
        final String lastName = mSignUpEditLastName.getText().toString().trim();
        String confirmPassword = mSignUpEditTextConfirmPassword.getText().toString().trim();
        final String pin = mSignUpEditTextPin.getText().toString().trim();

        // If the Email EditTextField is empty show an error and point where the error is by using .requestFocus
        if (email.isEmpty()) {
            mSignUpEditTextEmail.setError("Email is required");
            mSignUpEditTextEmail.requestFocus();
            return;
        } else if (firstName.isEmpty()) {
            mSignUpEditFirstName.setError("First name is required");
            mSignUpEditFirstName.requestFocus();
            return;
        } else if (lastName.isEmpty()) {
            mSignUpEditLastName.setError("Last name is required");
            mSignUpEditLastName.requestFocus();
            return;
        } else if (pin.isEmpty()) {
            mSignUpEditLastName.setError("PIN is required");
            mSignUpEditLastName.requestFocus();
            return;
        }
        /*
         If the Email in the EditTextField does not match with an already registered Email in Firebase,
         show an error and point where the error is by using .requestFocus
         */
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mSignUpEditTextEmail.setError("Please enter a valid email");
            mSignUpEditTextEmail.requestFocus();
            return;
        }
        // If the Password EditTextField is empty show an error and point where the error is by using .requestFocus
        if(password.isEmpty()) {
            mSignUpEditTextPassword.setError("Password is required");
            mSignUpEditTextPassword.requestFocus();
            return;
        }
        // If the Password EditTextField is not long enough, show an error and point where the error is by using .requestFocus
        if(password.length() < 6) {
            mSignUpEditTextPassword.setError("Minimum length of password should be 6");
            mSignUpEditTextPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            mSignUpEditTextPassword.setError(getString(Integer.parseInt("Passwords do not match")));
            mSignUpEditTextPassword.requestFocus();
        }

        /*
        This code uses the entry point for Firebase and uses the createUserWithEmailAndPassword method
        to create a user so it is saved in Firebase. For now it redirects to the MainScreenActivity,
        this we can just edit later.
         */
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser fireUser = mAuth.getCurrentUser(); //get user info
                            assert fireUser != null; // check if user != null
                            final String UID = fireUser.getUid(); //store user id

                            Map<String, Object> dbUser = new HashMap<>();
                            dbUser.put("first_name", firstName);
                            dbUser.put("last_name", lastName);
                            dbUser.put("PIN", pin);

                            // Add a new document with a generated ID
                            db.collection("users").document(UID).set(dbUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "User added with ID: " + UID);
                                }
                            });

                            //If everything succeeds, redirect to MainScreenActivity
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);

                     /*This method ensures that the 2 activities Login and Signup gets removed from the stack,
                        which means when you are logged in and press the back button, you will not get redirected
                        to the signup screen again, but will exit the app instead
                      */
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();

                            //If User is already registered, handle CollisionException and show a Toast message
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }


    /*
    This method onClick makes sure that when you click on the buttons,
    they do what they are supposed to do.
     */
    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            //When Sign up button is pressed, call the method registerUser
            case R.id.buttonSignUpLoginId:
                registerUser();
                break;
        }
    }


}