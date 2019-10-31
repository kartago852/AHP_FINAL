package com.example.ahp_final.result;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ahp_final.R;
import com.example.ahp_final.input.Criterio;
import com.example.ahp_final.model.ModeloVistaData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResultadoValorAlternativa extends AppCompatActivity {

    private ModeloVistaData modeloVistaData;

    private LinearLayout alternativaValorResultWrapper;
    private LinearLayout backToHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_resultado_valor_alternativa );

        bindData();
        initToolbar();
        initView();
        initBackToHomeButton();
    }

    private void bindData() {
        modeloVistaData = (ModeloVistaData) getIntent().getSerializableExtra(ModeloVistaData.DATA_KEY);
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        alternativaValorResultWrapper = findViewById(R.id.alternatif_result_wrapper);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TableLayout table = new TableLayout(this);
        table.setPadding(2, 2, 2, 10);

        Iterator<String> it = modeloVistaData.AlternativaValorMap.keySet().iterator();
        List<TableRow> descendingOptions = new ArrayList<TableRow>();

        while (it.hasNext()) {
            TableRow row = new TableRow(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            String alternativa = it.next();
            Float grade = modeloVistaData.AlternativaValorMap.get(alternativa);
            String percent = String.valueOf(Math.round(grade));

            TextView optionLabel = new TextView(this);
            optionLabel.setTextSize(24);
            optionLabel.setText(alternativa + ": ");
            optionLabel.setGravity(Gravity.START);
            optionLabel.setPadding(5, 5, 5, 5);

            TextView optionGrade = new TextView(this);
            optionGrade.setTextSize(24);
            optionGrade.setText(percent + "%");
            optionGrade.setGravity(Gravity.START);
            optionGrade.setPadding(5, 5, 5, 5);

            row.addView(optionLabel);
            row.addView(optionGrade);
            row.setTag(grade);

            if (descendingOptions.isEmpty()) {
                descendingOptions.add(row);
            } else {
                boolean inserted = false;
                for (int i = 0; i < descendingOptions.size(); i++) {
                    if (((Float) descendingOptions.get(i).getTag()) < grade) {
                        descendingOptions.add(i, row);
                        inserted = true;
                        break;
                    }
                }
                if (!inserted) {
                    descendingOptions.add(row);
                }
            }

        }

        for (TableRow option : descendingOptions) {
            table.addView(option);
        }
        layout.addView(table);

        ScrollView scroller = new ScrollView(this);
        scroller.addView(layout);
        alternativaValorResultWrapper.addView(scroller);
    }

    private void initBackToHomeButton() {
        backToHomeButton = findViewById(R.id.back_to_first_btn);
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeActivity();
                finish();
            }
        });
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(this, Criterio.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
