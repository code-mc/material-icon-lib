package net.steamcrafted.materialiconlib;

import android.content.Context;
import android.graphics.Typeface;

import java.io.File;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;

/**
 * Created by Wannes2 on 19/07/2015.
 */
public class MaterialIconUtils {
    private static final String mFontPath = "materialdesignicons-webfont.ttf";
    private static Typeface materialFont;
    private static Typeface materialFontEdit;

    private MaterialIconUtils() {
        // Prevents instantiation
    }

    static Typeface getTypeFace(Context context){
        if(materialFont == null) materialFont = Typeface.createFromAsset(context.getAssets(), mFontPath);
        return materialFont;
    }

    static int convertDpToPx(Context context, float dp) {
        return (int) applyDimension(COMPLEX_UNIT_DIP, dp,
                    context.getResources().getDisplayMetrics());
    }

    public static String getIconString(int iconIndex){
        return new String(Character.toChars(0xF101 + iconIndex));
    }
}
