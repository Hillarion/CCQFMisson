package com.devel.ccqf.ccqfmisson.Utilitairies;

/**
 * Created by jo on 6/1/16.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TypefaceUtil {

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     * @param defaultFontNameToOverride for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            System.out.print("Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }
    protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        Map<String, Typeface> newMap = new HashMap<String, Typeface>();
        newMap.put("sans-serif", newTypeface);
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField("sSystemFontMap");
            staticField.setAccessible(true);
            staticField.set(null, newMap);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
                e.printStackTrace();
        }

    }

}

