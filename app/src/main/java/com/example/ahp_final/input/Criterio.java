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
import com.example.ahp_final.view.MyItemView;
import com.google.android.material.textfield.TextInputEditText;
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

public class Criterio extends AppCompatActivity {

    private LinearLayout criterioContainer;
    private LinearLayout addCriterionBtn;
    private DatabaseReference mDatabase;
    private TextInputEditText itemField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_criterio );
        mDatabase = FirebaseDatabase.getInstance().getReference();

        bindView();

        initToolbar();
        initFirstKriteriaItem();
        initAddKriteria();
    }

    private void bindView() {
        criterioContainer = findViewById(R.id.criterio_container);
        addCriterionBtn = findViewById(R.id.criterio_btn);
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initFirstKriteriaItem() {
        final MyItemView myItemView = new MyItemView(this, 1, "Criterio # ");
        myItemView.setOnDeleteItemListener(new MyItemView.ItemViewListener() {
            @Override
            public void onDeleteItemListener() {
                criterioContainer.removeViewAt(myItemView.getPosition() - 1);
                updateItemPosition();
            }
        });
        criterioContainer.addView(myItemView);
        myItemView.requestItemFocus();
    }

    private void initAddKriteria() {
        addCriterionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = criterioContainer.getChildCount() + 1;
                final MyItemView myItemView = new MyItemView(Criterio.this, position, "Criterio #");
                myItemView.setOnDeleteItemListener(new MyItemView.ItemViewListener() {
                    @Override
                    public void onDeleteItemListener() {
                        criterioContainer.removeViewAt(myItemView.getPosition() - 1);
                        updateItemPosition();
                    }
                });
                criterioContainer.addView(myItemView);
                myItemView.requestItemFocus();
            }
        });
    }

    private void updateItemPosition() {
        for (int i = 0; i <= criterioContainer.getChildCount(); i++) {
            View child = criterioContainer.getChildAt(i);
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
            ViewUtils.tintMenuIcon(Criterio.this, menuItem, android.R.color.white);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_selesai) {
            String ids= mDatabase.push().getKey();
            mDatabase.child( "Criterios" ).child( ids ).setValue( getDataViewModel() );
            if (validateScreen()) {
                LaunchAlternatifScreen();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getValue() {
        return itemField.getText().toString().trim();
    }

    private void LaunchAlternatifScreen() {
        Intent intent = new Intent(this, Alternativa.class);
        intent.putExtra(ModeloVistaData.DATA_KEY, getDataViewModel());
        startActivity(intent);
    }

    private ModeloVistaData getDataViewModel() {
        HashMap<String, Float> criterioMap = new HashMap<>();
        ModeloVistaData keputusanViewModel = new ModeloVistaData();
        for (int i = 0; i <= criterioContainer.getChildCount(); i++) {
            View child = criterioContainer.getChildAt(i);
            if (child instanceof MyItemView) {
                criterioMap.put(((MyItemView) child).getValue(), 0f);
            }
        }

        keputusanViewModel.CriterioValorMap = criterioMap;
        return keputusanViewModel;
    }

    private boolean validateScreen() {
        return validateEmptyItem() && validateItem() && validateTwoItem() && validateSameItem();
    }

    private boolean validateItem() {
        boolean valid = true;
        for (int i = 0; i <= criterioContainer.getChildCount(); i++) {
            View child = criterioContainer.getChildAt(i);
            if (child instanceof MyItemView) {
                if (!((MyItemView) child).validateField()) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    private boolean validateEmptyItem() {
        boolean isItemNotEmpty = criterioContainer.getChildCount() > 0;
        if (!isItemNotEmpty) {
            Toast.makeText(this, "El criterio aún no existe, agregue el criterio", Toast.LENGTH_SHORT).show();
        }
        return isItemNotEmpty;
    }

    private boolean validateTwoItem() {
        boolean isTwoItems = criterioContainer.getChildCount() > 1;
        if (!isTwoItems) {
            Toast.makeText(this, "\n" +"Los criterios mínimos deben ser dos, agregue criterios", Toast.LENGTH_SHORT).show();
        }
        return isTwoItems;
    }

    private boolean validateSameItem() {
        List<String> list = new ArrayList<>();
        Set<String> hashSet = new HashSet<>();
        for (int i = 0; i <= criterioContainer.getChildCount(); i++) {
            View child = criterioContainer.getChildAt(i);
            if (child instanceof MyItemView) {
                String value = ((MyItemView) criterioContainer.getChildAt(i)).getValue().toLowerCase();
                list.add(value);
            }
        }
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);
        boolean valid = list.size() > 1;
        if (!valid) {
            Toast.makeText(this, "Los criterios no pueden ser los mismos.", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
}
