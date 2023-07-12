package com.example.tourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewRequest extends AppCompatActivity {
ImageView HoldList;
RecyclerView list;
FirebaseAuth auth;
    private ArrayList<Request> RequestList;
    private DatabaseReference reqRef;
    RequestAdapter requestAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        HoldList = findViewById(R.id.img);
        Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.hold);
        int nh = (int) (bitmapImage.getHeight() * (482.0 / bitmapImage.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 482, nh, true);
        HoldList.setImageBitmap(scaled);



        list = findViewById(R.id.ReqList);
        list.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        list.setLayoutManager(llm);
        list.setHasFixedSize(true);
        RequestList = new ArrayList<>();
        reqRef = FirebaseDatabase.getInstance().getReference();
        ClearAll();
        getDataFromFirebase();

    }

    private void ClearAll() {
        if(RequestList != null){
            RequestList.clear();

            if(requestAdapter != null) requestAdapter.notifyDataSetChanged();
        }
        RequestList = new ArrayList<>();
    }

    private void getDataFromFirebase() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String mUID = user.getUid();
        Query query = reqRef.child("Request").child(mUID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClearAll();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Request request = new Request();
                    if (dataSnapshot.child("name").exists()) {
                        request.setHotelName(dataSnapshot.child("name").getValue().toString());
                    }
                    if (dataSnapshot.child("location").exists()) {
                        request.setLocation(dataSnapshot.child("location").getValue().toString());
                    }
                    if (dataSnapshot.child("status").exists()) {
                        request.setStatus(dataSnapshot.child("status").getValue().toString());
                    }
                    RequestList.add(request);
                }
                Parcelable recViewPos = list.getLayoutManager().onSaveInstanceState();
                list.getLayoutManager().onRestoreInstanceState(recViewPos);
                requestAdapter = new RequestAdapter(getApplicationContext(),RequestList,mUID);
                list.setAdapter(requestAdapter);
                requestAdapter.notifyDataSetChanged();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}