package com.example.ahp_final.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;

import com.example.ahp_final.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private long backPressedTime;
    String email,pass;

    private CardView Login,Register;
    private FirebaseAuth firebaseAuth;
    private EditText TextEmail;
    private EditText TextPasss;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );


    }
}
