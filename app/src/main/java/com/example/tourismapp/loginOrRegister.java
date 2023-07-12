package com.example.tourismapp;


import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginOrRegister extends AppCompatActivity {
EditText email, pass;
Button signIn;
TextView signup;
ImageView img;
private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        signIn = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        img = findViewById(R.id.imageView2);
        auth = FirebaseAuth.getInstance();
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");

        Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.login1);
        int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
        img.setImageBitmap(scaled);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                String email_txt = email.getText().toString();
                String pass_txt = pass.getText().toString();
                if(TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(pass_txt)){
                    Toast.makeText(loginOrRegister.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
                }else{
                    login(email_txt,pass_txt);
                }
            }

            private void login(String email_txt, String pass_txt) {
                auth.signInWithEmailAndPassword(email_txt,pass_txt).addOnSuccessListener(authResult -> {
                    pd.dismiss();
                    Toast.makeText(loginOrRegister.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(loginOrRegister.this,userNavigate.class));
                    finish();
                });
                auth.signInWithEmailAndPassword(email_txt,pass_txt).addOnFailureListener(authResult -> Toast.makeText(loginOrRegister.this, "Invalid Credentials", Toast.LENGTH_SHORT).show());
            }
        });

        signup.setOnClickListener(view -> {
            Intent i = new Intent(loginOrRegister.this,signUp.class);
            startActivity(i);
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
            startActivity(new Intent(loginOrRegister.this,userNavigate.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
    }
}