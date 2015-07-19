package net.steamcrafted.materialiconlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Wannes2 on 19/07/2015.
 */
public class MaterialIconView extends TextView {
    private Rect bounds = new Rect();

    public MaterialIconView(Context context) {
        super(context);

        init();
    }

    public MaterialIconView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public MaterialIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(){
        setTypeface(MaterialIconUtils.getTypeFace(getContext()));
        setPadding(0,0,0,0);
    }


    private void init(Context context, AttributeSet attrs){
        init();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MaterialIconViewFormat);
        try {
            int type = array.getInt(R.styleable.MaterialIconViewFormat_materialIcon, 0);

            if(type >= 0) setIcon(type);
        } finally {
            array.recycle();
        }
    }

    private void setIcon(int iconIndex){
        setText(getIconString(iconIndex));
        invalidate();
    }

    public void setIcon(MaterialDrawableBuilder.IconValue iconValue){
        setText(getIconString(iconValue.ordinal()));
        invalidate();
    }

    public static String getIconString(int iconIndex){
        return new String(Character.toChars(0xF101 + iconIndex));
    }

    @Override
    public void onDraw(Canvas canvas){
        final CharSequence text = getText();

        TextPaint textPaint = getPaint();
        if(isInEditMode()){
            super.onDraw(canvas);
            return;
        }

        textPaint.setColor(getCurrentTextColor());
        textPaint.getTextBounds(text.toString(), 0, text.length(), bounds);

        int availWidth = canvas.getWidth();// - getTotalPaddingLeft() - getTotalPaddingRight();
        int availHeight = canvas.getHeight();// - getTotalPaddingTop() - getTotalPaddingBottom();

        int xPos = (availWidth - bounds.width()) / 2;
        int yPos = (availHeight - bounds.height()) / 2;
        int xOffset = xPos - bounds.left;
        int yOffset = yPos - bounds.top;

        bounds.offsetTo(xPos, yPos);

        canvas.drawText(text.toString(), xOffset, yOffset, textPaint);

        setText(text);
    }
}
