package com.app.yellodriver.util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TypefaceUtil {

    public static final int REGULAR = 1;
    public static final int BOLD = 2;
    public static final int ITALIC = 3;
    public static final int BOLD_ITALIC = 4;
    public static final int LIGHT = 5;
    public static final int CONDENSED = 6;
    public static final int THIN = 7;
    public static final int MEDIUM = 8;

    public static final String SANS_SERIF = "sans-serif";
    public static final String SANS_SERIF_LIGHT = "sans-serif-light";
    public static final String SANS_SERIF_CONDENSED = "sans-serif-condensed";
    public static final String SANS_SERIF_THIN = "sans-serif-thin";
    public static final String SANS_SERIF_MEDIUM = "sans-serif-medium";


    public static final String FIELD_DEFAULT = "DEFAULT";
    public static final String FIELD_SANS_SERIF = "SANS_SERIF";
    public static final String FIELD_SERIF = "SERIF";
    public static final String FIELD_DEFAULT_BOLD = "DEFAULT_BOLD";


    public static final String regularFontPath = "font/avenirnextltpro_regular.otf";
    public static final String boldFontPath = "font/avenirnextltpro_bold.otf";

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     *
     *
     */
    public static void overrideFont(Context context) {
        try {
            Typeface regular = getTypeface(REGULAR, context,regularFontPath);
            Typeface bold = getTypeface(BOLD, context,boldFontPath);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Map<String, Typeface> fonts = new HashMap<>();
                fonts.put(SANS_SERIF, regular);
                fonts.put(SANS_SERIF_LIGHT, regular);
                fonts.put(SANS_SERIF_CONDENSED, regular);
                fonts.put(SANS_SERIF_THIN, regular);
                fonts.put(SANS_SERIF_MEDIUM, bold);
                overrideFontsMap(fonts);
            } else {
                overrideFont(FIELD_SANS_SERIF, getTypeface(REGULAR, context, regularFontPath));
                overrideFont(FIELD_SERIF, getTypeface(REGULAR, context, regularFontPath));
            }
            overrideFont(FIELD_SERIF, getTypeface(REGULAR, context, regularFontPath));
            overrideFont(SANS_SERIF_MEDIUM, getTypeface(BOLD, context, boldFontPath));
            overrideFont(FIELD_DEFAULT, getTypeface(REGULAR, context, regularFontPath));
            overrideFont(FIELD_DEFAULT_BOLD, getTypeface(BOLD, context, boldFontPath));


            /*final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);*/
        } catch (Exception e) {
            e.printStackTrace();
            //YLog.e("Can not set custom font " , customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }


    /**
     * Using reflection to override default typefaces
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE
     * OVERRIDDEN
     *
     * @param typefaces map of fonts to replace
     */
    private static void overrideFontsMap(Map<String, Typeface> typefaces) {
        try {
            final Field field = Typeface.class.getDeclaredField("sSystemFontMap");
            field.setAccessible(true);
            Map<String, Typeface> oldFonts = (Map<String, Typeface>) field.get(null);
            if (oldFonts != null) {
                oldFonts.putAll(typefaces);
            } else {
                oldFonts = typefaces;
            }
            field.set(null, oldFonts);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            YLog.e("TypefaceUtil", "Can not set custom fonts, NoSuchFieldException");
        } catch (IllegalAccessException e) {
            YLog.e("TypefaceUtil", "Can not set custom fonts, IllegalAccessException");
        }
    }

    public static void overrideFont(String fontName, Typeface typeface) {
        try {
            final Field field = Typeface.class.getDeclaredField(fontName);
            YLog.e("Font replace",fontName);
            field.setAccessible(true);
            field.set(null, typeface);
            field.setAccessible(false);
        } catch (Exception e) {
            YLog.e("TypefaceUtil", "Can not set custom font " + typeface.toString() + " instead of " + fontName);
        }
    }

    public static Typeface getTypeface(int fontType, Context context, String customFontFileNameInAssets) {
        // here you can load the Typeface from asset or use default ones
        switch (fontType) {
            case BOLD:
                Typeface boldTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);
                return  boldTypeface;
            case ITALIC:
                Typeface italicTypeface = Typeface.createFromAsset(context.getAssets(), "font/avenirnextltpro_it.otf");
                return  italicTypeface;
            case LIGHT:
            case CONDENSED:
            case THIN:
            case MEDIUM:
            case REGULAR:
                /*return Typeface.create(SANS_SERIF_THIN, Typeface.NORMAL);*/
            default:
                Typeface regularTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);
                return regularTypeface;
        }
    }
}
