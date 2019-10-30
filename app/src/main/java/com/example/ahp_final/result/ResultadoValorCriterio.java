package com.example.ahp_final.result;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ahp_final.R;
import com.example.ahp_final.model.ModeloVistaData;
import com.example.ahp_final.value.ValorAlternativa;
import com.example.ahp_final.value.ValorCriterio;

/**
 * Created by Franco on 12/23/19.
 */

public class ResultadoValorCriterio extends AppCompatActivity {

    private LinearLayout okButton;
    private LinearLayout criterioResultContainer;

    private ModeloVistaData modeloVistaData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_resultado_valor_criterio );

        bindData();
        initToolbar();
        initResultView();
        initOkButton();
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void bindData() {
        modeloVistaData = (ModeloVistaData) getIntent().getSerializableExtra(ModeloVistaData.DATA_KEY);
    }

    @SuppressLint("SetTextI18n")
    private void initResultView() {
        criterioResultContainer = findViewById(R.id.kriteria_result_container);

        TableLayout tabel = new TableLayout(this);
        tabel.setPadding(2, 2, 2, 10);
        for (String criterio : modeloVistaData.CriterioValorMap.keySet()) {

            TableRow fila = new TableRow(this);
            fila.setOrientation(LinearLayout.HORIZONTAL);

            Float weight = modeloVistaData.CriterioValorMap.get(criterio);
            String porcentaje = String.valueOf(Math.round(weight * 100));

            TextView criterioLabel = new TextView(this);
            criterioLabel.setText(criterio + ": ");
            criterioLabel.setGravity(Gravity.END);
            criterioLabel.setPadding(5, 5, 5, 5);
            criterioLabel.setTextSize(24.0f);

            TextView criterioValue = new TextView(this);
            criterioValue.setText(porcentaje + "%");
            criterioValue.setGravity(Gravity.START);
            criterioValue.setPadding(5, 5, 5, 5);
            criterioValue.setTextSize(24.0f);

            fila.addView(criterioLabel);
            fila.addView(criterioValue);

            tabel.addView(fila);
        }
        criterioResultContainer.addView(tabel);
    }

    private void initOkButton() {
        okButton = findViewById(R.id.ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAlternatiValueScreen();
            }
        });
    }

    private void launchAlternatiValueScreen() {
        Intent intent = new Intent(this, ValorAlternativa.class);
        intent.putExtra(ModeloVistaData.DATA_KEY, modeloVistaData);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
