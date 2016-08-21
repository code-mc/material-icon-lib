package net.steamcrafted.materialiconlib;

import android.content.Context;
import android.graphics.Typeface;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;

/**
 * Created by Wannes2 on 19/07/2015.
 */
public class MaterialIconUtils {
    private static final String mFontPath = "materialdesignicons-webfont.ttf";
    private static Typeface materialFont;

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
        return new String(Character.toChars(0xF001 + iconIndex));
    }

    public static int getColorResource(Context context, int colorResource){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return context.getResources().getColor(colorResource, context.getTheme());
        }else{
            return context.getResources().getColor(colorResource);
        }
    }
}
