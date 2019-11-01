package com.example.ahp_final.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ahp_final.R;
import com.example.ahp_final.input.Criterio;

public class HomeActivity extends AppCompatActivity {

    public static final String user = "names";

    Button startBtn;
    TextView txtuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );


        txtuser = (TextView)findViewById( R.id.texser );
        String user = getIntent().getStringExtra("names");
        txtuser.setText( "!Bienvenido "+ user + "!" );

        initStartButton();
    }
    private void initStartButton() {
        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCriterioScreen();
            }
        });
    }

    private void launchCriterioScreen() {
        Intent intent = new Intent(this, Criterio.class);
        startActivity(intent);
    }
}
