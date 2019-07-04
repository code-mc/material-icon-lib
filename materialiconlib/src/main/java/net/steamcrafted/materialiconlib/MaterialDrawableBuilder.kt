package net.steamcrafted.materialiconlib

/**
 * Created by Wannes2 on 19/07/2015.
 */
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint

/**
 * Builder used to create a MaterialDrawable. Provide a context and at least an icon to build.
 *
 * Example usage:
 *
 *
 * <pre>
 * Drawable myDrawable = MaterialDrawableBuilder.with(context)
 * .setIcon(IconValue.ACCESS_POINT)
 * .setColor(Color.RED)
 * .setSizeDp(48)
 * .build();
</pre> *
 */
class MaterialDrawableBuilder
/**
 * Create an IconDrawable.
 *
 * @param context Your activity or application context.
 */
private constructor(private val context: Context) {

    private var icon: Int? = null

    private val paint: TextPaint

    private var size = -1

    var opacity = 255
        private set

    private val bounds = Rect()

    init {
        //this.icon = icon;
        paint = TextPaint()
        paint.typeface = MaterialIconUtils.getTypeFace(context)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.textAlign = Paint.Align.CENTER
        paint.isUnderlineText = false
        paint.color = Color.BLACK
        paint.isAntiAlias = true

        setToActionbarSize()
        setColor(Color.BLACK)
    }

    fun build(): Drawable {
        if (icon == null) {
            throw IconNotSetException()
        }
        return MaterialDrawable(icon, paint, size, opacity)
    }

    fun setIcon(iconValue: Int?): MaterialDrawableBuilder {

        //todo fixme : by Attect ,wrong 2848 robot_industrial ,but it can use.
        // dont't use setIcon(2848),use setIcon(IconValue.ROBOT_INDUSTRIAL) instead
        iconValue?.let {
            var realIconValue = it
            if (realIconValue > IconValue.ROBOT_INDUSTRIAL) realIconValue++
            icon = realIconValue
        }


        return this
    }

    /**
     * Set the size of this icon to the standard Android ActionBar.
     *
     * @return The current IconDrawable for chaining.
     */
    fun setToActionbarSize(): MaterialDrawableBuilder {
        return setSizeDp(ANDROID_ACTIONBAR_ICON_SIZE_DP)
    }

    /**
     * Set the size of the drawable.
     *
     * @param dimenRes The dimension resource.
     * @return The current IconDrawable for chaining.
     */
    fun setSizeResource(dimenRes: Int): MaterialDrawableBuilder {
        return setSizePx(context.resources.getDimensionPixelSize(dimenRes))
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in density-independent pixels (dp).
     * @return The current IconDrawable for chaining.
     */
    fun setSizeDp(size: Int): MaterialDrawableBuilder {
        return setSizePx(MaterialIconUtils.convertDpToPx(context, size.toFloat()))
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in pixels (px).
     * @return The current IconDrawable for chaining.
     */
    fun setSizePx(size: Int): MaterialDrawableBuilder {
        this.size = size
        bounds.set(0, 0, size, size)
        return this
    }

    /**
     * Set the color of the drawable.
     *
     * @param color The color, usually from android.graphics.Color or 0xFF012345.
     * @return The current IconDrawable for chaining.
     */
    fun setColor(color: Int): MaterialDrawableBuilder {
        paint.color = color
        setAlpha(Color.alpha(color))
        return this
    }

    /**
     * Set the color of the drawable.
     *
     * @param colorRes The color resource, from your R file.
     * @return The current IconDrawable for chaining.
     */
    fun setColorResource(colorRes: Int): MaterialDrawableBuilder {
        setColor(context.resources.getColor(colorRes))
        return this
    }

    fun setAlpha(alpha: Int): MaterialDrawableBuilder {
        this.opacity = alpha
        paint.alpha = alpha
        return this
    }

    fun setColorFilter(cf: ColorFilter): MaterialDrawableBuilder {
        paint.colorFilter = cf
        return this
    }

    fun clearColorFilter(): MaterialDrawableBuilder {
        paint.colorFilter = null
        return this
    }

    /**
     * Sets paint style.
     *
     * @param style to be applied
     */
    fun setStyle(style: Paint.Style): MaterialDrawableBuilder {
        paint.style = style
        return this
    }

    private inner class IconNotSetException : RuntimeException {

        @JvmOverloads
        constructor(message: String = "No icon provided when building MaterialDrawable.") : super(message)

        constructor(cause: Throwable) : super(cause)

        constructor(message: String, cause: Throwable) : super(message, cause)
    }

    private inner class MaterialDrawable
    /**
     * Create a MaterialDrawable.
     *
     * @param icon    The icon you want this drawable to display.
     */
    (private val icon: Int?, private val paint: TextPaint, size: Int, var iconAlpha: Int) : Drawable() {
        private var size = -1
        private val mCachedRect = Rect()

        init {
            this.setSizePx(size)

            invalidateSelf()
        }

        /**
         * Set the size of the drawable.
         *
         * @param size The size in pixels (px).
         * @return The current IconDrawable for chaining.
         */
        fun setSizePx(size: Int): MaterialDrawable {
            this.size = size
            setBounds(0, 0, size, size)
            invalidateSelf()
            return this
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

        override fun getIntrinsicHeight(): Int {
            return size
        }

        override fun getIntrinsicWidth(): Int {
            return size
        }

        override fun draw(canvas: Canvas) {
            // Center drawable within available bounds
            val boundsWidth = bounds.width()
            val boundsHeight = bounds.height()
            val dimen = Math.min(boundsWidth, boundsHeight)

            paint.textSize = dimen.toFloat()
            val textValue = MaterialIconUtils.getIconString(icon!!)
            paint.getTextBounds(textValue, 0, 1, mCachedRect)
            val textBottom = bounds.top.toFloat() + (boundsHeight - mCachedRect.height()) / 2f + mCachedRect.height().toFloat() - mCachedRect.bottom

            canvas.drawText(textValue, bounds.left + boundsWidth / 2f, textBottom, paint)
        }

        override fun isStateful(): Boolean {
            return true
        }

        override fun setState(stateSet: IntArray): Boolean {
            val oldValue = paint.alpha
            val newValue = iconAlpha//Utils.isEnabled(stateSet) ? alpha : alpha / 2;
            paint.alpha = newValue
            return oldValue != newValue
        }

        override fun setAlpha(alpha: Int) {
            this.iconAlpha = alpha
            paint.alpha = alpha
        }

        override fun setColorFilter(cf: ColorFilter?) {
            paint.colorFilter = cf
        }

        override fun clearColorFilter() {
            paint.colorFilter = null
        }

        override fun getOpacity(): Int {
            return PixelFormat.UNKNOWN
        }

        /**
         * Sets paint style.
         *
         * @param style to be applied
         */
        fun setStyle(style: Paint.Style) {
            paint.style = style
        }

    }

    companion object {
        val ANDROID_ACTIONBAR_ICON_SIZE_DP = 24

        @JvmStatic
        fun with(context: Context): MaterialDrawableBuilder {
            return MaterialDrawableBuilder(context)
        }
    }

}