package com.example.ahp_final.value;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ahp_final.R;
import com.example.ahp_final.model.ModeloVistaData;
import com.example.ahp_final.result.ResultadoValorAlternativa;
import com.example.ahp_final.utils.Matrix;
import com.example.ahp_final.utils.StringUtils;
import com.example.ahp_final.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValorAlternativa extends AppCompatActivity {

    private static final float DEFAULT_RATING = 0f;
    private ModeloVistaData modeloVistaData;

    private LinearLayout alternativaValorContainer;

    private Matrix matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_valor_alternativa );

        bindData();
        bindView();
        initToolbar();
    }


    @SuppressLint("SetTextI18n")
    private void bindView() {
        this.matrix = new Matrix(modeloVistaData.AlternativaValorMap.keySet(),
                modeloVistaData.CriterioValorMap.keySet());

        alternativaValorContainer = findViewById(R.id.alternatif_bobot_container);
        LinearLayout layoutContainer = new LinearLayout(this);
        layoutContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        for (String option : modeloVistaData.AlternativaValorMap.keySet()) {
            TextView label = new TextView(this);
            label.setText("Alternativa :" + option);
            label.setGravity(Gravity.START);
            label.setTextSize(28);
            label.setTypeface(Typeface.DEFAULT_BOLD);
            label.setPadding(8, 16, 8, 8);
            layout.addView(label);

            List<LinearLayout> bars = generateRatingBars(option, modeloVistaData.CriterioValorMap.keySet());
            for (LinearLayout bar : bars) {
                layout.addView(bar);
            }
        }

        layoutContainer.addView(layout);
        ScrollView scroller = new ScrollView(this);
        scroller.addView(layoutContainer);
        alternativaValorContainer.addView(scroller);
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
    private List<LinearLayout> generateRatingBars(String option, Set<String> kriteriaSet) {
        List<LinearLayout> bars = new ArrayList<>();
        for (String kriteria : kriteriaSet) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            TextView kriteriaLabel = new TextView(this);
            kriteriaLabel.setText(kriteria + ": ");
            kriteriaLabel.setTextSize(20);
            kriteriaLabel.setGravity(Gravity.START);
            kriteriaLabel.setTypeface(Typeface.DEFAULT_BOLD);
            kriteriaLabel.setPadding(8, 32, 8, 8);
            row.addView(kriteriaLabel);
            row.addView(createRatingBar(option, kriteria));

            bars.add(row);
        }

        return bars;
    }

    private RatingBar createRatingBar(String option, String kriteria) {
        final RatingBar bar = new RatingBar(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(36, 8, 0, 8);
        bar.setTag(StringUtils.encodeStringPair(option, kriteria));
        bar.setNumStars(5);
        bar.setStepSize(0.5f);
        bar.setRating(DEFAULT_RATING);
        bar.setLayoutParams(params);
        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String[] labels = StringUtils.decodeStringPair((String) bar.getTag());
                String rowLabel = labels[0];
                String colLabel = labels[1];
                ValorAlternativa.this.matrix.setValue(rowLabel, colLabel, rating);
                Log.i(this.getClass().getName(), ValorAlternativa.this.matrix.toString());
            }
        });
        this.matrix.setValue(option, kriteria, DEFAULT_RATING);

        return bar;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        MenuItem menuItem = menu.findItem(R.id.action_selesai);
        if (menuItem != null) {
            ViewUtils.tintMenuIcon(ValorAlternativa.this, menuItem, android.R.color.white);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_selesai) {
            launchAlternativaValorResultScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchAlternativaValorResultScreen() {
        Intent intent = new Intent(this, ResultadoValorAlternativa.class);
        intent.putExtra(ModeloVistaData.DATA_KEY, getDataAlternativa());
        startActivity(intent);
    }

    private ModeloVistaData getDataAlternativa() {
        for (String alternativa : modeloVistaData.AlternativaValorMap.keySet()) {
            float valor = 0;
            for (String criterio : modeloVistaData.CriterioValorMap.keySet()) {
                float weight = modeloVistaData.CriterioValorMap.get(criterio);
                float rating = this.matrix.getValue(alternativa, criterio) * 20; // 5 estrella completa = 100%
                valor += rating * weight;
            }
            modeloVistaData.AlternativaValorMap.put(alternativa, valor);
        }
        return modeloVistaData;
    }
}
