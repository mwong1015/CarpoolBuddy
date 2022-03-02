package com.example.carpoolbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class signUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    private EditText userNameField;
    private EditText phoneNumberField;
    private String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        userNameField = (EditText) findViewById(R.id.userNameEditText);
        phoneNumberField = (EditText) findViewById(R.id.PhoneNumberEditText);
        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        role = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void signUp(View v){
        ArrayList<String> emptyArrayList = new ArrayList<String>();
        String userName = userNameField.getText().toString();
        String phoneNumber = phoneNumberField.getText().toString();
        User newUser = new User(userName,role, "1000", emptyArrayList, emptyArrayList, phoneNumber, mAuth.getCurrentUser().getUid()); // if user is not valid then make a new user using the currentUser UID

        fireStore.collection("Users").document(mAuth.getCurrentUser().getUid()).set(newUser);
        startActivity(new Intent(this, userProfileActivity.class));
        Toast.makeText(this, "Sign up successful", Toast.LENGTH_LONG).show();
    }


}