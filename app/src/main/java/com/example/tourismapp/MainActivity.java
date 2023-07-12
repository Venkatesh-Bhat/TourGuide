package com.example.tourismapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//Button add;
//private final int GAL_REQ_CODE = 1000;
//ImageView imgGal;


//    PRACTICE
//    TreeMap<String,List> loc = new TreeMap<>();
//    List<Integer> locImg = Arrays.asList(R.drawable.ayodhya,R.drawable.dwarka,R.drawable.hampi);
//    List<Integer> locRat = Arrays.asList(5,4,5);
//    List<String> locName =Arrays.asList("Ayodhya","Dwarka","Hampi");
//    List<String> locPlace = Arrays.asList("Uttar Pradesh, India","Gujarat, India","Karnataka, India");

//    Firebase
    private DatabaseReference myRef;

    private ArrayList<locations> locationsArrayList;
    private locsAdapter locationAdapter;
    RecyclerView locRV;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference wishRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchItem = findViewById(R.id.searchItem);
        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

            private void filter(String text) {
                ArrayList<locations> filteredList= new ArrayList<>();
                for(locations item : locationsArrayList){
                    if(item.getLoc_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(item);
                    }
                }

                locationAdapter.filterList(filteredList);
            }
        });

//        loc.put("imgName",locImg);
//        loc.put("locRat",locRat);
//        loc.put("locName",locName);
//        loc.put("locPlace",locPlace);


//        HashMap<String,Object> map = new HashMap<>();
//        map.put("Name","Venkatesh");
//        map.put("PhoneNo","9876543210");
//        FirebaseDatabase.getInstance().getReference().child("Database_name").child("Branch_name").updateChildren(map);

        locRV = findViewById(R.id.locRV);
        locRV.setNestedScrollingEnabled(false);

//        below line is for setting a layout manager for our recycler view.
//        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

//       IN BELOW 2 LINES WE ARE SETTING LAYOUT MANAGER AND ADAPTER TO OUR RECYCLER VIEW
        locRV.setLayoutManager(llm);
        locRV.setHasFixedSize(true);



        myRef = FirebaseDatabase.getInstance().getReference();

        wishRef = myRef.getDatabase().getReference("Wishlist");
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
            wishRef = wishRef.child(currentUserId);
        }


        locationsArrayList = new ArrayList<>();
        ClearAll();

        getDataFromFirebase();

    }

    private void getDataFromFirebase() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();
        Query query = myRef.child("Tourism").child("Locations");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClearAll();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    locations loc = new locations();
                    loc.setImage(snapshot1.child("image").getValue().toString());
                    loc.setLoc_name(snapshot1.child("name").getValue().toString());
                    loc.setRating(snapshot1.child("rating").getValue().toString());
                    loc.setLoc_place(snapshot1.child("place").getValue().toString());

                    loc.setWishlist(Boolean.parseBoolean(snapshot1.child("Wishlist").child(currentUserId).getValue().toString()));
                    locationsArrayList.add(loc);
                }
                pd.dismiss();
                 Parcelable recViewPos = locRV.getLayoutManager().onSaveInstanceState();
                locRV.getLayoutManager().onRestoreInstanceState(recViewPos);
                locationAdapter = new locsAdapter(getApplicationContext(),wishRef,locationsArrayList);
                locRV.setAdapter(locationAdapter);
                locationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void ClearAll(){
        if(locationsArrayList != null){
            locationsArrayList.clear();

            if(locationAdapter != null) locationAdapter.notifyDataSetChanged();
        }
        locationsArrayList = new ArrayList<>();
    }

}



