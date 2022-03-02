package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class vehicleProfileActivity extends AppCompatActivity {

    FirebaseFirestore fireStore;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Vehicle thisVehicle;

    private ImageView carImageView;
    private StorageReference storageRef;
    private Button reserveOrCloseButton;
    private String ownerString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vehicle_profile);
        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        getIncomingIntent();
        carImageView = (ImageView) findViewById(R.id.imageView);
        reserveOrCloseButton = (Button) findViewById(R.id.Button);
        storageRef = FirebaseStorage.getInstance().getReference();
        setUpButton();
        setUpProfile();

    }

    public void getIncomingIntent(){
        thisVehicle = (Vehicle) getIntent().getSerializableExtra("Vehicle");
        ownerString = getIntent().getStringExtra("owner");
    }

    public void setUpButton(){
        setReserveButton();
        setCloseButton();
    }

    private void setReserveButton(){
        if(!(ownerString.equals(thisVehicle.getOwner()))){ // this is user uid and vehicle uuid, they are different. what i can do is get user name
            reserveOrCloseButton.setText("Reserve");
            reserveOrCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fireStore.collection("Vehicles").document(thisVehicle.getUUID())
                            .update("capacity", String.valueOf(Integer.parseInt(thisVehicle.getCapacity())-1))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(vehicleProfileActivity.this, "Reserve successful", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(vehicleProfileActivity.this, vehiclesInfoActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(vehicleProfileActivity.this, "Reserve failed", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            });
        }
    }

    private void setCloseButton(){
        if(ownerString.equals(thisVehicle.getOwner())){
            reserveOrCloseButton.setText("Close Vehicle");
            reserveOrCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fireStore.collection("Vehicles").document(thisVehicle.getUUID())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(vehicleProfileActivity.this, "Close successful", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(vehicleProfileActivity.this, vehiclesInfoActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(vehicleProfileActivity.this, "Close failed", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            });
        }
    }

    private void setUpProfile() {
        TextView ownerTextView = findViewById(R.id.OwnerTextView);
        ownerTextView.setText("Owner: " + thisVehicle.getOwner());
        TextView priceTextView = findViewById(R.id.PriceTextView);
        priceTextView.setText("Price: " + thisVehicle.getPrice());
        TextView carModelTextView = findViewById(R.id.CarModelTextView);
        carModelTextView.setText("Car Model: " + thisVehicle.getCarModel());
        TextView capacityTextView = findViewById(R.id.CapacityTextView);
        capacityTextView.setText("Capacity: " + thisVehicle.getCapacity());
        if (!(thisVehicle.getURL() == "")) {
            StorageReference photoRef = storageRef.child(thisVehicle.getURL());
            final long ONE_MEGABYTE = 1024 * 1024;
            photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    carImageView.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}

