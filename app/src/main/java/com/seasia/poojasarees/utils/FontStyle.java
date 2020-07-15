package com.seasia.poojasarees.utils;

import android.graphics.Typeface;
import android.util.Log;


import com.seasia.poojasarees.application.MyApplication;

import java.util.HashMap;

/**
 //* Created by Saira on 03/07/2019.
 */


public class FontStyle {
   private static FontStyle customFontFamily;
    final HashMap<String, String> fontMap = new HashMap<>();


    public static FontStyle getInstance() {
        if (customFontFamily == null)
            customFontFamily = new FontStyle();
        return customFontFamily;
    }

    public void addFont(String alias, String fontName) {
        fontMap.put(alias, fontName);
    }

    public Typeface getFont(String alias) {
        String fontFilename = fontMap.get(alias);
        if (fontFilename == null) {
            Log.e("", "Font not available with name " + alias);
            return null;
        } else {
            Typeface typeface = Typeface.createFromAsset(MyApplication.instance.getAssets(), "fonts/" + fontFilename);
            return typeface;
        }
    }
}


