package com.example.tourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class signUp extends AppCompatActivity {
    private FirebaseAuth auth;
    Button register;
    EditText pass1, pass2, email;
    ImageView signMan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        register = findViewById(R.id.register);
        pass1 = findViewById(R.id.pass1);
        pass2 = findViewById(R.id.pass2);
        email = findViewById(R.id.email);
        auth = FirebaseAuth.getInstance();
        signMan = findViewById(R.id.signImg);
        Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.signman);
        int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
        signMan.setImageBitmap(scaled);


        final ProgressDialog pd = new ProgressDialog(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_txt = email.getText().toString();
                String pass_txt = pass1.getText().toString();
                String confirm_pass = pass2.getText().toString();
                if (TextUtils.equals(pass_txt, confirm_pass) && !TextUtils.isEmpty(email_txt) && !TextUtils.isEmpty(pass_txt) && !TextUtils.isEmpty(confirm_pass)) {
                    registerUser(email_txt, pass_txt);
                }
                else if (TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(pass_txt) || TextUtils.isEmpty(confirm_pass)) {
                    Toast.makeText(signUp.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
                } else if (pass_txt.length() < 6) {
                    Toast.makeText(signUp.this, "Min length of Password is 6", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.equals(pass_txt,confirm_pass)) {
                    Toast.makeText(signUp.this, "Enter same Password", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(email_txt, pass_txt);
                }
            }

            private void registerUser(String email_txt, String pass_txt) {

                pd.setMessage("Wait for a moment");
                pd.show();
                    auth.createUserWithEmailAndPassword(email_txt,pass_txt).addOnCompleteListener(signUp.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                                FirebaseUser CurrentUsr = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference wishRef = myRef.child("Tourism").child("Locations");
                                String currentUID = CurrentUsr.getUid();
                                wishRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot childSnapshot : snapshot.getChildren()){
                                            String childKey = childSnapshot.getKey();

                                            wishRef.child(childKey).child("Wishlist").child(currentUID).setValue(false);
//                                            snapshot1.child("Wishlist").child(currentUID);
//                                            wishRef.child("Wishlist").child(currentUID).setValue(false);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                pd.dismiss();
                                Toast.makeText(signUp.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signUp.this,loginOrRegister.class));
                                finish();
                            }
                            else{
                                Toast.makeText(signUp.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            }
        });
    }
}