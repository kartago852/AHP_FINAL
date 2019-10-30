package com.example.ahp_final.value;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.example.ahp_final.R;
import com.example.ahp_final.model.ModeloVistaData;
import com.example.ahp_final.result.ResultadoValorAlternativa;
import com.example.ahp_final.result.ResultadoValorCriterio;
import com.example.ahp_final.utils.Matrix;
import com.example.ahp_final.utils.StringUtils;
import com.example.ahp_final.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValorCriterio extends AppCompatActivity {

    private ModeloVistaData modeloVistaData = null;
    private LinearLayout criterioValueContainer;

    private static final String[] spinnerLabelsTemplate = {
            " absolutamente no más importante que(0.11) ", // absolutamente no más importante que
            " muy no más importante qu(0.14) ", // muy no más importante que
            " más sin importancia que(0.2) ", // más sin importancia que
            " bastante sin importancia que(0.33) ", // bastante sin importancia que
            " es igual de importante que(1) ", // es igual de importante que
            " es moderadamente importante que(3) ", // es moderadamente importante que
            " es fuertemente importante que(5) ", // es fuertemente importante que
            " es muy fuertemente importante que(7) ", // es muy fuertemente importante que
            " es extremadamente importante que (9)"}; // es extremadamente importante que
    private static final float[] spinnerValues = {1f / 9f, 1f / 7f, 1f / 5f, 1f / 3f, 1f, 3f, 5f, 7f, 9f};
    private static final int DEFAULT_SPINNER_VALUE_POSITION = 4;
    private static final float DEFAULT_SPINNER_VALUE = spinnerValues[DEFAULT_SPINNER_VALUE_POSITION];

    private Matrix matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_valor_criterio );

        initToolbar();
        bindData();
        bindView();

    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void bindData() {
        modeloVistaData = (ModeloVistaData) getIntent().getSerializableExtra(ModeloVistaData.DATA_KEY);
    }

    private void bindView(){
        List<String> comparisons = generateCriterioPairs(modeloVistaData.CriterioValorMap.keySet());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        criterioValueContainer = findViewById(R.id.criterio_valor_container);
        for (String comparison : comparisons) {
            Spinner spinner = createSpinner(comparison);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 40);
            spinner.setLayoutParams(layoutParams);
            layout.addView(spinner);
        }
        ScrollView scroller = new ScrollView(this);
        scroller.addView(layout);
        criterioValueContainer.addView(scroller);
    }

    private Spinner createSpinner(String codeCriterioPar) {
        String[] criterio = StringUtils.decodeStringPair(codeCriterioPar);

        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<CharSequence>(
                        this,
                        android.R.layout.simple_spinner_item,
                        generatePickerDisplayedValues(criterio[0], criterio[1]));
        adapter.setDropDownViewResource(R.layout.multiline_spinner_dropdow_item);

        Spinner spinner = new Spinner(this);
        spinner.setTag(codeCriterioPar);
        spinner.setAdapter(adapter);
        spinner.setSelection(DEFAULT_SPINNER_VALUE_POSITION);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String encodedPair = (String) parent.getTag();
                String[] criterio = StringUtils.decodeStringPair(encodedPair);
                String filaCriterio = criterio[0];
                String columnaCriterio = criterio[1];
                float normalValue = spinnerValues[position];
                float invertedValue = 1 / normalValue;

                ValorCriterio.this.matrix.setValue(filaCriterio, columnaCriterio, normalValue);
                ValorCriterio.this.matrix.setValue(columnaCriterio, filaCriterio, invertedValue);

                Log.d("matriz", "matriz: " + ValorCriterio.this.matrix.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return spinner;
    }

    private String[] generatePickerDisplayedValues(String leftKriteria, String rightKriteria) {
        String[] values = new String[spinnerLabelsTemplate.length];
        for (int i = 0; i < spinnerLabelsTemplate.length; i++) {
            values[i] = leftKriteria + spinnerLabelsTemplate[i] + rightKriteria;
        }
        return values;
    }

    private List<String> generateCriterioPairs(Set<String> criterioSet) {
        this.matrix = new Matrix(criterioSet, criterioSet);
        List<String> comparisons = new ArrayList<>();
        Object[] criterio = criterioSet.toArray();
        for (int i = 0; i < criterio.length; i++) {
            String filasCriterio = (String) criterio[i];
            this.matrix.setValue(filasCriterio, filasCriterio, DEFAULT_SPINNER_VALUE);

            for (int j = i + 1; j < criterio.length; j++) {
                String columnasCriterio = (String) criterio[j];
                this.matrix.setValue(filasCriterio, columnasCriterio, DEFAULT_SPINNER_VALUE);
                this.matrix.setValue(columnasCriterio, filasCriterio, DEFAULT_SPINNER_VALUE);

                String normalEncoding = StringUtils.encodeStringPair(filasCriterio, columnasCriterio);
                comparisons.add(normalEncoding);
            }
        }

        return comparisons;
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
            ViewUtils.tintMenuIcon(ValorCriterio.this, menuItem, android.R.color.white);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_selesai) {
            Intent intent = new Intent(this, ResultadoValorCriterio.class);
            intent.putExtra(ModeloVistaData.DATA_KEY, getDataResult());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ModeloVistaData getDataResult() {
        Map<String, Float> totalColumna = this.matrix.getTotalColumna();
        this.matrix.divideMatrixBySumOfColumn(totalColumna);
        Map<String, Float> value = this.matrix.getSumMatrix();

        for (String criterio : value.keySet()) {
            Float valueFila = value.get(criterio);
            modeloVistaData.CriterioValorMap.put(criterio, valueFila);
        }
        return modeloVistaData;
    }


}
