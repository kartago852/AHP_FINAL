package com.example.ahp_final.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

@SuppressLint("ViewConstructor")
public class MyItemView extends LinearLayout {

    private int position = 0;
    private String hint = "";

    public MyItemView(Context context, int position, String hint){
        super(context);
    }

}
