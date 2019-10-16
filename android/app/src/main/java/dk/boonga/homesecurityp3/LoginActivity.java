package dk.boonga.homesecurityp3;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    FirebaseAuth mAuth;
    CallbackManager mCallbackManager;

    EditText mLoginEditTextEmail, mLoginEditTextPassword;
    TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginEditTextEmail = findViewById(R.id.editTextEmailId);
        mLoginEditTextPassword = findViewById(R.id.editTextPasswordId);
        mSignUpTextView = findViewById(R.id.textViewSignUpId);

        findViewById(R.id.textViewSignUpId).setOnClickListener(this);
        findViewById(R.id.buttonLoginId).setOnClickListener(this);
    }
    /*
       This method is used to login the user
        */
    public void userLogin() {
        /*
        This gets the text written by the user in the EditTextFields email and password.
         */
        String email = mLoginEditTextEmail.getText().toString().trim();
        String password = mLoginEditTextPassword.getText().toString().trim();

        // If the Email EditTextField is empty show an error and point where the error is by using .requestFocus
        if (email.isEmpty()) {
            mLoginEditTextEmail.setError("Email is required");
            mLoginEditTextEmail.requestFocus();
            return;
        }

        /*
         If the Email in the EditTextField does not match with an already registered Email in Firebase,
         show an error and point where the error is by using .requestFocus
         */
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mLoginEditTextEmail.setError("Please enter a valid email");
            mLoginEditTextEmail.requestFocus();
            return;
        }

        // If the Password EditTextField is empty show an error and point where the error is by using .requestFocus
        if (password.isEmpty()) {
            mLoginEditTextPassword.setError("Password is required");
            mLoginEditTextPassword.requestFocus();
            return;
        }

        // If the Password EditTextField is not long enough, show an error and point where the error is by using .requestFocus
        if (password.length() < 6) {
            mLoginEditTextPassword.setError("Minimum length of password should be 6");
            mLoginEditTextPassword.requestFocus();
            return;
        }
        //LoginProgressBar.setVisibility(View.VISIBLE);

        /*
        This code uses the entry point for Firebase and uses the signInWithEmailAndPassword method
        to log in a user which is already created in Firebase. For now it redirects to the MainScreenActivity,
        this we can just edit later.
         */

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
       This method onClick makes sure that when you click on the buttons,
       they do what they are supposed to do.
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            //When button "Sign up" is pressed, redirect to SignUpActivity
            case R.id.textViewSignUpId:
                startActivity(new Intent(this, SignUpActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            //When button Login is pressed, call the method mergeFacebookToEmail
            case R.id.buttonLoginId:
                userLogin();
                break;
            case R.id.forgotPasswordId:
//                startActivity(new Intent(this, ResetPasswordActivity.class),
//                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
        }
    }
}