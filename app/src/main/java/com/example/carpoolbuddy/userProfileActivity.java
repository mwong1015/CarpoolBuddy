package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class userProfileActivity extends AppCompatActivity {

    FirebaseUser mUser;
    FirebaseAuth mAuth;
    // other layout objects
    private StorageReference storageRef;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        storageRef = FirebaseStorage.getInstance().getReference();
        imageView = (ImageView) findViewById(R.id.seeVehicleImageView);
        setUpUI();
    }

    private void setUpUI(){
        StorageReference photoRef = storageRef.child("1644211426108.jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signOut(View v){
        mAuth.getInstance().signOut();
        Intent intent = new Intent (this, authActivity.class);
        startActivity(intent);
    }

    public void seeVehicles(View v){
        Intent intent = new Intent(this, vehiclesInfoActivity.class);
        startActivity(intent);
    }
}