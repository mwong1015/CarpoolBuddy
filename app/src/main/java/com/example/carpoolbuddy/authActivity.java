package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

public class authActivity extends AppCompatActivity {

    private GoogleSignInAccount mGoogleSignInAccount;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    private String selected;
    private Spinner sUserType;
    // other layout objects

    private EditText emailField;
    private EditText passwordField;
    private static int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        fireStore = FirebaseFirestore.getInstance();
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("331666368560-mn64at4bgep693dahmfosiihbrjcu6gr.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    // create the classes
    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    checkUserValidity(task);
                    Toast.makeText(authActivity.this, "Google signed up successfully", Toast.LENGTH_LONG).show();
                }else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(authActivity.this, "Google sign up failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signUp(View v) {
        System.out.println("sign up");
        String emailString = emailField.getText().toString();
        if(!(emailString.contains(".cis.edu.hk"))){
            Toast.makeText(this, "Only CIS users can use this app", Toast.LENGTH_SHORT).show();
            return;
        }
        String passwordString = passwordField.getText().toString();
        mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(authActivity.this, "You signed up successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(authActivity.this, signUpActivity.class));
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(authActivity.this, "Sign up failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signIn(View v) {
        String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();
        mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(authActivity.this, "You signed in successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(authActivity.this, userProfileActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(authActivity.this, "sign in failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    protected void checkUserValidity(Task<AuthResult> task){
        if(task.getResult().getAdditionalUserInfo().isNewUser()){
            startActivity(new Intent(authActivity.this, signUpActivity.class));
        }
        else{
            updateUI(mAuth.getCurrentUser());
        }
    }
    protected void updateUI(FirebaseUser currentUser) {
        if(!(currentUser == null)){
            startActivity(new Intent(this, userProfileActivity.class));
        }
    }
}