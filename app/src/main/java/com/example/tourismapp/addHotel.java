package com.example.tourismapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class addHotel extends AppCompatActivity {
    StorageReference storageRef;
    String[] options;
    String selectedCity;
    EditText hotelName,hotelPrice,phoneNo;
    List<Uri> selectedImages;
    DatabaseReference dbRef;
    FirebaseAuth auth;
    String currentUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);
        Button selectImageButton = findViewById(R.id.primImgBtn);
        hotelName = findViewById(R.id.hotelName);
        hotelPrice = findViewById(R.id.hotelPrice);
        phoneNo = findViewById(R.id.phone);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        currentUID = currentUser.getUid();

        RecyclerView imageRecyclerView = findViewById(R.id.imageRecyclerView);
        selectedImages = new ArrayList<>();
        AtomicReference<ImageAdapter> imageAdapter = new AtomicReference<>(new ImageAdapter(selectedImages, getApplicationContext()));
        imageRecyclerView.setNestedScrollingEnabled(false);
        GridLayoutManager llm = new GridLayoutManager(this,3);
        imageRecyclerView.setLayoutManager(llm);
        imageRecyclerView.setHasFixedSize(true);

        dbRef = FirebaseDatabase.getInstance().getReference();
        Query query = dbRef.child("Tourism").child("Locations");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int)snapshot.getChildrenCount();
                int i = 0;
                options = new String[count];
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    options[i] = dataSnapshot.child("name").getValue().toString();
                    i++;
                }
                Spinner mySpinner = findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(addHotel.this, android.R.layout.simple_spinner_item, options);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mySpinner.setAdapter(adapter);

                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Perform actions based on the selected item
                        selectedCity = options[position];

                        // Do something with the selected item
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle the case where no item is selected
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ActivityResultLauncher<String[]> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenMultipleDocuments(),
                result -> {
                    List<Uri> newSelectedImages = new ArrayList<>(result);
                    ImageAdapter newImageAdapter = new ImageAdapter(newSelectedImages, getApplicationContext());
                    selectedImages.clear();
                    selectedImages.addAll(newSelectedImages);
                    imageAdapter.set(newImageAdapter);
                    imageRecyclerView.setAdapter(imageAdapter.get());
//                    uploadImagesToFirebase(result);
                });

        selectImageButton.setOnClickListener(v -> {
            // Open the image picker
            imagePickerLauncher.launch(new String[]{"image/*"});
            imageRecyclerView.setAdapter(imageAdapter.get());
        });

    }

    public void register(View view) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();
        String HotelName = hotelName.getText().toString();
        String HotelPrice = hotelPrice.getText().toString();
        String PhoneNo = phoneNo.getText().toString();
        if(HotelName.isEmpty() || HotelPrice.isEmpty() || PhoneNo.isEmpty() || selectedImages.isEmpty()){
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
        }


        else {
            storageRef = FirebaseStorage.getInstance().getReference();

            double rating = Math.random() * (4.8 - 3 + 1) + 3;
            DecimalFormat decForm = new DecimalFormat("0.0");
            DatabaseReference reqRef = dbRef.child("Request").child(currentUID).child(HotelName);
            reqRef.child("name").setValue(HotelName);
            reqRef.child("price").setValue("Rs " + HotelPrice + " / 2 guests");
            reqRef.child("phone").setValue(PhoneNo);
            reqRef.child("status").setValue("waiting");
            reqRef.child("location").setValue(selectedCity);
            reqRef.child("rating").setValue(decForm.format(rating));

            // Upload images
            int totalImages = selectedImages.size();
            List<Task<Uri>> uploadTasks = new ArrayList<>();

            for (int i = 0; i < totalImages; i++) {
                Uri imageUri = selectedImages.get(i);
                StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());
                UploadTask uploadTask = imageRef.putFile(imageUri);

                Task<Uri> uploadTaskUri = uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                });
                uploadTasks.add(uploadTaskUri);
            }
            Task<List<Uri>> allUploadsTask = Tasks.whenAllSuccess(uploadTasks);
            allUploadsTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Uri> downloadUris = task.getResult();
                    for (int i = 0; i < downloadUris.size(); i++) {
                        Uri uri = downloadUris.get(i);
                        String imgUrl = uri.toString();
                        reqRef.child("imgUrls").child("img" + i).setValue(imgUrl);
                    }
                    // All images uploaded, proceed to the next activity
                    Toast.makeText(addHotel.this, "Your request has been sent!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ViewRequest.class));
                    pd.dismiss();
                    finish();
                } else {
                    // Handle upload failure
                    Exception exception = task.getException();
                    Toast.makeText(addHotel.this, "Failed to upload images: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}