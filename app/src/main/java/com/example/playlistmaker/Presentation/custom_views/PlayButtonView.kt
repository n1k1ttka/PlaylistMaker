package com.example.playlistmaker.Presentation.custom_views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R

class PlayButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
): View(context, attrs, defStyleAttr, defStyleRes) {

    private var imageBitmap: Bitmap?
    private var playImage: Bitmap? = null
    private var pauseImage: Bitmap? = null
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private val minViewSize = resources.getDimensionPixelSize(R.dimen.playButtonViewMinSize)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlayButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playImage = getDrawable(R.styleable.PlayButtonView_playingImage)?.toBitmap()
                pauseImage = getDrawable(R.styleable.PlayButtonView_pauseImage)?.toBitmap()

                imageBitmap = playImage
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            else -> minViewSize
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            else -> minViewSize
        }

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        imageBitmap?.let {
            canvas.drawBitmap(it, null, imageRect, null)
        }
    }

    fun setPlaying(isPlaying: Boolean) {
        imageBitmap = if (!isPlaying) playImage else pauseImage
        invalidate()
    }
}