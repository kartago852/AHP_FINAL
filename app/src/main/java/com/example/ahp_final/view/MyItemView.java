package com.example.ahp_final.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ahp_final.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



/**
 * Created by wisnu on 12/23/17.
 */

@SuppressLint("ViewConstructor")
public class MyItemView extends LinearLayout {

    private int position = 0;
    private String hint = "";

    private TextInputEditText itemField;
    private TextInputLayout itemFieldWrapper;
    private ImageView deleteButton;

    DatabaseReference mDatabase;

    private ItemViewListener onDeleteItemListener;

    public MyItemView(Context context, int position, String hint){
        super(context);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.AppTheme);
        LayoutInflater layoutInflater = LayoutInflater.from(contextThemeWrapper);
        View view = layoutInflater.inflate(R.layout.item_field, this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        itemField = view.findViewById(R.id.item_field);
        itemFieldWrapper = view.findViewById(R.id.item_field_wrapper);
        deleteButton = view.findViewById(R.id.delete_item_btn);

        setItemHint(hint);
        setPosition(position);
        initDeleteButton();


    }

    void setItemHint(String hint) {
        this.hint = hint;
    }

    public void setPosition(int position) {
        this.position = position;
        itemFieldWrapper.setHint(hint + position);
    }

    private void initDeleteButton() {
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemField.clearFocus();
                if (onDeleteItemListener != null) {
                    onDeleteItemListener.onDeleteItemListener();
                }
            }
        });
    }

    public void setOnDeleteItemListener(ItemViewListener onDeleteItemListener) {
        this.onDeleteItemListener = onDeleteItemListener;
    }

    public String getValue() {
        return itemField.getText().toString().trim();
    }

    public int getPosition() {
        return position;
    }

    public boolean validateField() {
        String text = itemField.getText().toString();
        if (text.trim().isEmpty()) {
            itemFieldWrapper.setError("Campo Vacio");
            itemFieldWrapper.setErrorEnabled(true);
            return false;
        }

        itemFieldWrapper.setError(null);
        itemFieldWrapper.setErrorEnabled(false);
        return true;
    }
    public void requestItemFocus() {
        itemField.requestFocus();
    }

    public interface ItemViewListener {
        void onDeleteItemListener();
    }

}
