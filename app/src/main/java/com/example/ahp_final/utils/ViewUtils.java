package com.example.ahp_final.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import androidx.annotation.ColorRes;
import androidx.core.graphics.drawable.DrawableCompat;

public class ViewUtils {

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color){
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }
}
