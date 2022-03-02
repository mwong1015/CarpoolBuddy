package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class vehiclesInfoActivity extends AppCompatActivity {

    FirebaseFirestore fireStore;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Vehicle vehicleInfo;
    RecyclerView recView;
    ArrayList<Vehicle> vehiclesList = new ArrayList<Vehicle>();
    vehicleRecyclerViewAdapter myAdapter;
    ArrayList<String> ownerList;
    ArrayList<String> capacityList;
    String ownerString;
    // other layout objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_info);
        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recView = findViewById(R.id.recView);
        getOwnerString();
    }

    public void getAndPopulateData(View v){
        vehiclesList = new ArrayList<Vehicle>();
        CollectionReference vehicleRef = fireStore.collection("Vehicles");
        fireStore.collection("Vehicles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot ds : task.getResult()) {
                        Vehicle thisVehicle = ds.toObject(Vehicle.class);
                        vehiclesList.add(thisVehicle);
                    }
                    myAdapter = new vehicleRecyclerViewAdapter(vehiclesList, vehiclesInfoActivity.this, ownerString);
                    recView.setAdapter(myAdapter);
                }
            }
        });
        recView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void getOwnerString(){
        fireStore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot ds : task.getResult()) {
                        if (mAuth.getUid().equals(ds.toObject(User.class).getUID())) {
                            ownerString = ds.toObject(User.class).getUserName();
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(vehiclesInfoActivity.this, "get owner failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goToAddVehicle(View v){
        Intent intent = new Intent(this, addVehicleActivity.class);
        startActivity(intent);
    }
}
