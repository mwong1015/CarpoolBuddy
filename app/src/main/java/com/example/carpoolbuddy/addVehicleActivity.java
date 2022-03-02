package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class addVehicleActivity extends AppCompatActivity {

    // UI textEditObjects

    FirebaseFirestore fireStore;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    // other layout objects

    private EditText owner;
    private EditText carModel;
    private EditText capacity;
    private EditText price;

    String ownerString;
    String carModelString;
    String capacityString;
    String priceString;
    String ID;

    public Uri mImageUri;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageTask mUploadTask;
    private Button uploadButton;
    Long fileName;
    Vehicle thisVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_vehicle);
        fireStore = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        carModel = (EditText) findViewById(R.id.editTextCarModel);
        capacity = (EditText) findViewById(R.id.editTextCapacity);
        price = (EditText) findViewById(R.id.editTextPrice);
        uploadButton = (Button) findViewById(R.id.uploadImageButton);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        fileName = System.currentTimeMillis();

    }

    protected boolean formValid() {
        carModelString = carModel.getText().toString();
        capacityString = capacity.getText().toString();
        priceString = price.getText().toString();
        try{
            int capacity = Integer.parseInt(capacityString);
            int price = Integer.parseInt(priceString);
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Capacity and price must be an integer", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    public void addNewVehicle(View v) {
        if (formValid()) {
            fireStore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot ds : task.getResult()) {
                            if (mAuth.getUid().equals(ds.toObject(User.class).getUID())) {
                                ownerString = ds.toObject(User.class).getUserName();
                                if (mImageUri != null){
                                    thisVehicle = new Vehicle(ownerString, carModelString, capacityString, priceString, UUID.randomUUID().toString(),fileName + "." + getFileExtension(mImageUri));
                                }
                                else{
                                    thisVehicle = new Vehicle(ownerString, carModelString, capacityString, priceString, UUID.randomUUID().toString(),"");
                                }
                                fireStore.collection("Vehicles").document(thisVehicle.getUUID()).set(thisVehicle);
                                fireStore.collection("Users").document(ds.toObject(User.class).getUID()).update(
                                        "ownedVehicles", (ds.toObject(User.class).getOwnedVehicleList().add(thisVehicle.getUUID()))
                                );
                                Toast.makeText(addVehicleActivity.this, "Vehicle added", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            });
            startActivity(new Intent (this, vehiclesInfoActivity.class));
        }
    }
    public void selectImage(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data!= null && data.getData()!=null) {
            mImageUri = data.getData();

            uploadPicture();
            }
        }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();
        if (mImageUri != null) {
            StorageReference fileReference = storageRef.child(fileName
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                            uploadButton.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pd.setMessage("Progress " + (int) progressPercent + " %");
                            if (progressPercent == 100.00) {
                                pd.dismiss();
                            }
                        }
                    });
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
