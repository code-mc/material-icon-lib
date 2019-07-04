package net.steamcrafted.materialiconlib

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

/**
 * Created by Wannes2 on 21/07/2015.
 */
class MaterialIconView : ImageView {

    private val mBuilder: MaterialDrawableBuilder by lazy { MaterialDrawableBuilder.with(context) }
    private var mDrawable: Drawable? = null
    private var mIcon: Int? = null
    private var mOverruledSize = -1

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {

        init(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mDrawable == null) {
            var size = MaterialIconUtils.convertDpToPx(context, 24f)
            val width = MeasureSpec.getMode(widthMeasureSpec)
            val height = MeasureSpec.getMode(heightMeasureSpec)

            val paddinghori = paddingLeft + paddingRight
            val paddingvert = paddingTop + paddingBottom

            if (width == MeasureSpec.UNSPECIFIED && height == MeasureSpec.UNSPECIFIED) {
                // do nothing, just default 24 dp size
            } else if (width == MeasureSpec.UNSPECIFIED) {
                size = MeasureSpec.getSize(heightMeasureSpec) - paddingvert
            } else if (height == MeasureSpec.UNSPECIFIED) {
                size = MeasureSpec.getSize(widthMeasureSpec) - paddinghori
            } else {
                size = Math.min(MeasureSpec.getSize(heightMeasureSpec) - paddingvert,
                        MeasureSpec.getSize(widthMeasureSpec) - paddinghori)
            }
            size = Math.max(0, size)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            regenerateDrawable()
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }

    }


    private fun init(context: Context, attrs: AttributeSet) {

        val array = context.obtainStyledAttributes(attrs, R.styleable.MaterialIconViewFormat)
        try {
            val type = array.getInt(R.styleable.MaterialIconViewFormat_materialIcon, 0)

            if (type >= 0) setIcon(type)
        } catch (e: Exception) {

        }

        try {
            val color = array.getColor(R.styleable.MaterialIconViewFormat_materialIconColor, Color.BLACK)

            setColor(color)
        } catch (e: Exception) {

        }

        try {
            val size = array.getDimensionPixelSize(R.styleable.MaterialIconViewFormat_materialIconSize, -1)

            if (size >= 0) setSizePx(size)
        } catch (e: Exception) {

        }

        array.recycle()
    }


    fun setIcon(iconValue: Int?) {
        mIcon = iconValue
        mBuilder.setIcon(iconValue)
        regenerateDrawable()
    }

    /**
     * Set the size of this icon to the standard Android ActionBar.
     */
    fun setToActionbarSize() {
        setSizeDp(ACTIONBAR_HEIGHT_DP)
    }

    /**
     * Set the size of the drawable.
     *
     * @param dimenRes The dimension resource.
     */
    fun setSizeResource(dimenRes: Int) {
        mBuilder.setSizeResource(dimenRes)
        mOverruledSize = context.resources.getDimensionPixelSize(dimenRes)
        regenerateDrawable()
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in density-independent pixels (dp).
     */
    fun setSizeDp(size: Int) {
        mBuilder.setSizeDp(size)
        mOverruledSize = MaterialIconUtils.convertDpToPx(context, size.toFloat())
        regenerateDrawable()
    }

    /**
     * Set the size of the drawable.
     *
     * @param size The size in pixels (px).
     */
    fun setSizePx(size: Int) {
        mBuilder.setSizePx(size)
        mOverruledSize = size
        regenerateDrawable()
    }

    /**
     * Set the color of the drawable.
     *
     * @param color The color, usually from android.graphics.Color or 0xFF012345.
     */
    fun setColor(color: Int) {
        mBuilder.setColor(color)
        regenerateDrawable()
    }

    /**
     * Set the color of the drawable.
     *
     * @param colorRes The color resource, from your R file.
     */
    fun setColorResource(colorRes: Int) {
        mBuilder.setColorResource(colorRes)
        regenerateDrawable()
    }

    /**
     * Sets paint style.
     *
     * @param style to be applied
     */
    fun setStyle(style: Paint.Style) {
        mBuilder.setStyle(style)
        regenerateDrawable()
    }

    private fun regenerateDrawable() {
        if (mIcon != null) {
            mDrawable = mBuilder.build()
            super.setImageDrawable(mDrawable)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (width == 0 || height == 0) return

        val scaledWidth = measuredWidth
        val scaledHeight = measuredHeight
        val scaleddimen = if (mOverruledSize >= 0) mOverruledSize else Math.min(scaledHeight, scaledWidth)

        var redraw = false
        if (mDrawable == null) {
            redraw = true
        } else {
            val initialdimen = Math.min(mDrawable!!.intrinsicHeight, mDrawable!!.intrinsicHeight)
            if (initialdimen != scaleddimen) {
                redraw = true
            }
        }

        if (redraw) {
            if (mOverruledSize >= 0) {
                mBuilder.setSizePx(mOverruledSize)
            } else {
                mBuilder.setSizePx(scaleddimen)
            }
            regenerateDrawable()
        }

        super.onDraw(canvas)
    }

    companion object {

        private val ACTIONBAR_HEIGHT_DP = 24
    }


}