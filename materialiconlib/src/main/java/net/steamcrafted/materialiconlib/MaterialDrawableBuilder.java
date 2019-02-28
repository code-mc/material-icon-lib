package net.steamcrafted.materialiconlib;

/**
 * Created by Wannes2 on 19/07/2015.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

/**
 * Builder used to create a MaterialDrawable. Provide a context and at least an icon to build.
 *
 * Example usage:
 * <p></p>
 * <pre>
 *     Drawable myDrawable = MaterialDrawableBuilder.with(context)
 *              .setIcon(IconValue.ACCESS_POINT)
 *              .setColor(Color.RED)
 *              .setSizeDp(48)
 *     .build();
 * </pre>
 */
public class MaterialDrawableBuilder {
    //keep original use
    public static IconValue IconValue = new IconValue();

    public static final int ANDROID_ACTIONBAR_ICON_SIZE_DP = 24;

    private final Context context;

    private Integer icon = null;

    private TextPaint paint;

    private int size = -1;

    private int alpha = 255;

    private final Rect bounds = new Rect();

    /**
     * Create an IconDrawable.
     *
     * @param context Your activity or application context.
     */
    private MaterialDrawableBuilder(Context context) {
        this.context = context;
        //this.icon = icon;
        paint = new TextPaint();
        paint.setTypeface(MaterialIconUtils.getTypeFace(context));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setUnderlineText(false);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        setToActionbarSize();
        setColor(Color.BLACK);
    }

    public static MaterialDrawableBuilder with(Context context){
        return new MaterialDrawableBuilder(context);
    }

    public Drawable build() throws IconNotSetException{
        if(icon == null){
            throw new IconNotSetException();
        }
        return new MaterialDrawable(icon, paint, size, alpha);
    }

    public MaterialDrawableBuilder setIcon(Integer iconValue) {
        //todo fixme : by Attect ,wrong 2848 robot_industrial ,but it can use.
        // dont't use setIcon(2848),use setIcon(IconValue.ROBOT_INDUSTRIAL) instead
        if (iconValue > IconValue.ROBOT_INDUSTRIAL) iconValue++;
        icon = iconValue;
        return this;
    }

    /**
     * Set the size of this icon to the standard Android ActionBar.
     *
     * @return The current IconDrawable for chaining.
     */
    public MaterialDrawableBuilder setToActionbarSize() {
        return setSizeDp(ANDROID_ACTIONBAR_ICON_SIZE_DP);
    }

    /**
     * Set the size of the drawable.
     *
     * @param dimenRes The dimension resource.
     * @return The current IconDrawable for chaining.
     */
    public MaterialDrawableBuilder setSizeResource(int dimenRes) {
        return setSizePx(context.getResources().getDimensionPixelSize(dimenRes));
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in density-independent pixels (dp).
     * @return The current IconDrawable for chaining.
     */
    public MaterialDrawableBuilder setSizeDp(int size) {
        return setSizePx(MaterialIconUtils.convertDpToPx(context, size));
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in pixels (px).
     * @return The current IconDrawable for chaining.
     */
    public MaterialDrawableBuilder setSizePx(int size) {
        this.size = size;
        bounds.set(0, 0, size, size);
        return this;
    }

    /**
     * Set the color of the drawable.
     *
     * @param color The color, usually from android.graphics.Color or 0xFF012345.
     * @return The current IconDrawable for chaining.
     */
    public MaterialDrawableBuilder setColor(int color) {
        paint.setColor(color);
        setAlpha(Color.alpha(color));
        return this;
    }

    /**
     * Set the color of the drawable.
     *
     * @param colorRes The color resource, from your R file.
     * @return The current IconDrawable for chaining.
     */
    public MaterialDrawableBuilder setColorResource(int colorRes) {
        setColor(context.getResources().getColor(colorRes));
        return this;
    }

    public MaterialDrawableBuilder setAlpha(int alpha) {
        this.alpha = alpha;
        paint.setAlpha(alpha);
        return this;
    }

    public MaterialDrawableBuilder setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
        return this;
    }

    public MaterialDrawableBuilder clearColorFilter() {
        paint.setColorFilter(null);
        return this;
    }

    public int getOpacity() {
        return this.alpha;
    }

    /**
     * Sets paint style.
     *
     * @param style to be applied
     */
    public MaterialDrawableBuilder setStyle(Paint.Style style) {
        paint.setStyle(style);
        return this;
    }

    private class IconNotSetException extends RuntimeException {
        public IconNotSetException() {
            this("No icon provided when building MaterialDrawable.");
        }

        public IconNotSetException(String message)
        {
            super(message);
        }

        public IconNotSetException(Throwable cause)
        {
            super(cause);
        }

        public IconNotSetException(String message, Throwable cause)
        {
            super(message, cause);
        }
    }

    private class MaterialDrawable extends Drawable {
        private Integer icon;
        private TextPaint paint;
        private int size = -1;
        private int alpha = 255;

        /**
         * Create a MaterialDrawable.
         *
         * @param icon    The icon you want this drawable to display.
         */
        public MaterialDrawable(Integer icon, TextPaint paint, int size, int alpha) {
            this.icon = icon;
            this.paint = paint;
            this.setSizePx(size);
            this.setAlpha(alpha);

            invalidateSelf();
        }

        /**
         * Set the size of the drawable.
         *
         * @param size The size in pixels (px).
         * @return The current IconDrawable for chaining.
         */
        public MaterialDrawable setSizePx(int size) {
            this.size = size;
            setBounds(0, 0, size, size);
            invalidateSelf();
            return this;
        }

        /*
        public MaterialDrawable setIcon(Integer iconin){
            this.icon = iconin;
            invalidateSelf();
            return this;
        }

        public MaterialDrawable setTextPaint(TextPaint p){
            this.paint = p;
            invalidateSelf();
            return this;
        }*/

        @Override
        public int getIntrinsicHeight() {
            return size;
        }

        @Override
        public int getIntrinsicWidth() {
            return size;
        }

        private final Rect mCachedRect = new Rect();
        @Override
        public void draw(Canvas canvas) {
            // Center drawable within available bounds
            int boundsWidth = getBounds().width();
            int boundsHeight = getBounds().height();
            int dimen = Math.min(boundsWidth, boundsHeight);

            paint.setTextSize(dimen);
            String textValue = MaterialIconUtils.getIconString(icon);
            paint.getTextBounds(textValue, 0, 1, mCachedRect);
            float textBottom = getBounds().top + (boundsHeight - mCachedRect.height()) / 2f + mCachedRect.height() - mCachedRect.bottom;

            canvas.drawText(textValue, getBounds().left + boundsWidth / 2f, textBottom, paint);
        }

        @Override
        public boolean isStateful() {
            return true;
        }

        @Override
        public boolean setState(int[] stateSet) {
            int oldValue = paint.getAlpha();
            int newValue = alpha;//Utils.isEnabled(stateSet) ? alpha : alpha / 2;
            paint.setAlpha(newValue);
            return oldValue != newValue;
        }

        @Override
        public void setAlpha(int alpha) {
            this.alpha = alpha;
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            paint.setColorFilter(cf);
        }

        @Override
        public void clearColorFilter() {
            paint.setColorFilter(null);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }

        /**
         * Sets paint style.
         *
         * @param style to be applied
         */
        public void setStyle(Paint.Style style) {
            paint.setStyle(style);
        }

    }

}