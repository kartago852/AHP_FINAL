package com.example.ahp_final.result;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Franco on 12/23/19.
 */

public class ResultadoValorCriterio extends AppCompatActivity {

    private PieChart pieChart;


    private LinearLayout okButton;
    private LinearLayout criterioResultContainer;

    private ModeloVistaData modeloVistaData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_resultado_valor_criterio );

        bindData();
        initToolbar();
        //initResultView();
        pieChart();
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
        criterioResultContainer = findViewById(R.id.criterio_result_container);

        TableLayout table = new TableLayout(this);
        table.setPadding(2, 2, 2, 10);
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

            table.addView(fila);
        }
        criterioResultContainer.addView(table);
    }

    // METODO GRAFICO CIRCULAR
    private void pieChart(){

        pieChart = findViewById( R.id.piechart );

        pieChart.setUsePercentValues( true );
        pieChart.getDescription().setEnabled( false );
        pieChart.setExtraOffsets( 5,10,5,5 );

        pieChart.setDragDecelerationFrictionCoef( 0.99f );

        pieChart.setDrawHoleEnabled( true );
        pieChart.setHoleColor( Color.BLUE );
        pieChart.setTransparentCircleRadius( 60f );
        pieChart.setCenterTextSize( 20f );
        pieChart.setCenterText( "Criterios" );

        pieChart.animateY( 1000, Easing.EasingOption.EaseInOutCubic );


        List<PieEntry> pieEntries = new ArrayList<>();

        for(String criterio : modeloVistaData.CriterioValorMap.keySet()){

            Float weight = modeloVistaData.CriterioValorMap.get(criterio);
            String porcentaje = String.valueOf(Math.round(weight * 100));
            pieEntries.add( new PieEntry( Integer.parseInt(porcentaje), criterio) );

        }

        PieDataSet dataSet = new PieDataSet( pieEntries, ": Criterios" );
        Legend legend = pieChart.getLegend();
        legend.setTextSize( 30f );

        dataSet.setSliceSpace( 1f );
        dataSet.setSelectionShift( 5f );
        dataSet.setColors( ColorTemplate.JOYFUL_COLORS );

        PieData data = new PieData( (dataSet) );
        data.setValueTextSize( 20f );
        data.setValueTextColor( Color.BLACK );

        pieChart.setData( data );
    }

    // FIN

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
