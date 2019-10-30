package com.example.ahp_final.input;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ahp_final.R;
import com.example.ahp_final.model.ModeloVistaData;
import com.example.ahp_final.utils.ViewUtils;
import com.example.ahp_final.value.ValorAlternativa;
import com.example.ahp_final.value.ValorCriterio;
import com.example.ahp_final.view.MyItemView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Franco on 12/23/19.
 */

public class Alternativa extends AppCompatActivity {

    private ModeloVistaData modeloVistaData = null;

    private LinearLayout alternativaContainer;
    private LinearLayout addAlternativaBtn;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_alternativa );
        mDatabase = FirebaseDatabase.getInstance().getReference();

        bindData();
        bindView();

        initToolbar();
        initFirstAlternatifItem();
        initAddAlternatifButton();

    }

    private void bindData() {
        modeloVistaData = (ModeloVistaData) getIntent().getSerializableExtra(ModeloVistaData.DATA_KEY);
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void bindView() {
        alternativaContainer = findViewById(R.id.alternativa_container);
        addAlternativaBtn = findViewById(R.id.alternativa_btn);
    }

    private void initFirstAlternatifItem() {
        final MyItemView myItemView = new MyItemView(this, 1, "Alternativa # ");
        myItemView.setOnDeleteItemListener(new MyItemView.ItemViewListener() {
            @Override
            public void onDeleteItemListener() {
                alternativaContainer.removeViewAt(myItemView.getPosition() - 1);
                updateItemPosition();
            }
        });
        alternativaContainer.addView(myItemView);
        myItemView.requestItemFocus();
    }

    private void initAddAlternatifButton() {
        addAlternativaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = alternativaContainer.getChildCount() + 1;
                final MyItemView myItemView = new MyItemView(Alternativa.this, position, "Alternativa");
                myItemView.setOnDeleteItemListener(new MyItemView.ItemViewListener() {
                    @Override
                    public void onDeleteItemListener() {
                        alternativaContainer.removeViewAt(myItemView.getPosition() - 1);
                        updateItemPosition();
                    }
                });
                alternativaContainer.addView(myItemView);
                myItemView.requestItemFocus();
            }
        });
    }

    private void updateItemPosition() {
        for (int i = 0; i <= alternativaContainer.getChildCount(); i++) {
            View child = alternativaContainer.getChildAt(i);
            if (child instanceof MyItemView) {
                ((MyItemView) child).setPosition(i + 1);
            }
        }
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
            ViewUtils.tintMenuIcon(Alternativa.this, menuItem, android.R.color.white);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_selesai) {
            if (validateScreen()) {
                String ida= mDatabase.push().getKey();
                mDatabase.child( "Alternativas" ).child( ida ).setValue( getDataViewModelAlter() );
                launchAlternativaValueScreen();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchAlternativaValueScreen() {
        Intent intent = new Intent(this, ValorCriterio.class);
        intent.putExtra(ModeloVistaData.DATA_KEY, getDataViewModel());
        startActivity(intent);
    }

    private ModeloVistaData getDataViewModel() {
        HashMap<String, Float> alternatifMap = new HashMap<>();
        for (int i = 0; i <= alternativaContainer.getChildCount(); i++) {
            View child = alternativaContainer.getChildAt(i);
            if (child instanceof MyItemView) {
                alternatifMap.put(((MyItemView) child).getValue(), 0f);
            }
        }
        modeloVistaData.AlternativaValorMap = alternatifMap;
        return modeloVistaData;
    }

    private ModeloVistaData getDataViewModelAlter() {
        HashMap<String, Float> alternativaMap = new HashMap<>();
        ModeloVistaData modeloVistaData = new ModeloVistaData();
        for (int i = 0; i <= alternativaContainer.getChildCount(); i++) {
            View child = alternativaContainer.getChildAt(i);
            if (child instanceof MyItemView) {
                alternativaMap.put(((MyItemView) child).getValue(), 0f);
            }
        }
        modeloVistaData.AlternativaValorMap = alternativaMap;
        return modeloVistaData;
    }

    private boolean validateScreen() {
        return validateEmptyItem() && validateItem() && validateTwoItem() && validateSameItem();
    }

    private boolean validateItem() {
        boolean valid = true;
        for (int i = 0; i <= alternativaContainer.getChildCount(); i++) {
            View child = alternativaContainer.getChildAt(i);
            if (child instanceof MyItemView) {
                if (!((MyItemView) child).validateField()) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    private boolean validateEmptyItem() {
        boolean isItemNotEmpty = alternativaContainer.getChildCount() > 0;
        if (!isItemNotEmpty) {
            Toast.makeText(this, "Ingrese Alternativas", Toast.LENGTH_SHORT).show();
        }
        return isItemNotEmpty;
    }

    private boolean validateTwoItem() {
        boolean isTwoItems = alternativaContainer.getChildCount() > 1;
        if (!isTwoItems) {
            Toast.makeText(this, "Minimo dos Alternativas", Toast.LENGTH_SHORT).show();
        }
        return isTwoItems;
    }

    private boolean validateSameItem() {
        List<String> list = new ArrayList<>();
        Set<String> hashSet = new HashSet<>();
        for (int i = 0; i <= alternativaContainer.getChildCount(); i++) {
            View child = alternativaContainer.getChildAt(i);
            if (child instanceof MyItemView) {
                String value = ((MyItemView) alternativaContainer.getChildAt(i)).getValue().toLowerCase();
                list.add(value);
            }
        }
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);
        boolean valid = list.size() > 1;
        if (!valid) {
            Toast.makeText(this, "Error, Alternativas Iguales", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
}
