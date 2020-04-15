package com.zuiweng.test.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.text.StaticLayout
import android.text.TextDirectionHeuristic
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.zuiweng.test.utils.dp2px
import com.zuiweng.test.utils.screenWidth
import io.zhuozhuo.remotetestlib.Message


class TextAndImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val padStart = dp2px(8f)
    private val padEnd = dp2px(8f)
    private val padTop = dp2px(8f)
    private val padBottom = dp2px(8f)

    //<editor-fold desc="设置View的绘制Type属性">
    private var type = Message.MessageTypeText
    fun setType(type: Int) {
        this.type = type
    }
    //</editor-fold>

    //<editor-fold desc="文本属性设置">
    private val textPaint: TextPaint by lazy {
        TextPaint().apply {
            this.isAntiAlias = true
            this.textAlign = Paint.Align.RIGHT
            this.color = Color.RED
            this.textSize = dp2px(18f).toFloat()
            this.flags = Paint.ANTI_ALIAS_FLAG
        }
    }
    private val bgPaint: TextPaint by lazy {
        TextPaint().apply {
            this.isAntiAlias = true
            this.textAlign = Paint.Align.LEFT
            this.color = Color.BLUE
            this.flags = Paint.ANTI_ALIAS_FLAG
        }
    }

    private var maxLineWidth = 0f
    private var maxTextLayoutHeight = 0f

    @RequiresApi(Build.VERSION_CODES.M)
    var textLayout: StaticLayout? = null

    @RequiresApi(Build.VERSION_CODES.M)
    fun setText(text: String) {
        if (text.isEmpty()) {
            textLayout = null
            maxLineWidth = 0f
            maxTextLayoutHeight = 0f
            this.requestLayout()
        } else {
            textLayout = StaticLayout.Builder.obtain(
                    text,
                    0,
                    text.length,
                    textPaint,
                    screenWidth() - padStart - padEnd
                ).setTextDirection(object : TextDirectionHeuristic {
                    override fun isRtl(array: CharArray?, start: Int, count: Int) = false

                    override fun isRtl(cs: CharSequence?, start: Int, count: Int) = false

                })
                .build()
            maxLineWidth = textLayout!!.maxLineWidth()
            maxTextLayoutHeight = textLayout!!.height.toFloat()
            this.requestLayout()

        }

    }

    //</editor-fold>
    private var bitmap: Bitmap? = null
    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        this.requestLayout()
        this.invalidate()
    }

    //<editor-fold desc="设置View的高度">
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (type == Message.MessageTypeText) {
            setMeasuredDimension(
                screenWidth(),
                maxTextLayoutHeight.toInt() + padTop + padBottom
            )
        } else {
            setMeasuredDimension(screenWidth(), (bitmap?.height ?: 0) + padTop + padBottom)
        }
    }
    //</editor-fold>

    //<editor-fold desc="绘制文本显示、图片显示">
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        //清空canvas
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        if (type == Message.MessageTypeText) {
            drawTestText(canvas)
        } else {
            drawTestBitmap(canvas)
        }
    }

    //</editor-fold>

    //<editor-fold desc="绘制文本">
    private fun drawTestText(canvas: Canvas) {
        if (textLayout == null) return
        //绘制文本背景
        canvas.drawRect(
            (screenWidth() - padEnd).toFloat() - maxLineWidth,
            padTop.toFloat(),
            (screenWidth() - padEnd).toFloat(),
            padTop + (textLayout!!.height).toFloat(),
            bgPaint
        )
        //绘制文字、且右对齐
        canvas.save()
        canvas.translate((width - padEnd).toFloat(), padTop * 1f)
        for (index in 0 until textLayout!!.lineCount) {
            canvas.drawText(
                textLayout!!.text,
                textLayout!!.getLineStart(index),
                textLayout!!.getLineEnd(index),
                textLayout!!.getLineLeft(index),
                textLayout!!.getLineBaseline(index).toFloat(),
                textPaint
            )
        }
//        canvas.drawText(textLayout!!.text)
//        textLayout!!.draw(canvas)
        canvas.restore()
    }

    //</editor-fold>

    //<editor-fold desc="绘制图片">
    private fun drawTestBitmap(canvas: Canvas) {
        if (this.bitmap == null) return
        canvas.drawBitmap(
            this.bitmap!!,
            (screenWidth() - bitmap!!.width - padEnd).toFloat(),
            paddingTop.toFloat(),
            null
        )
    }
    //</editor-fold>

    //<editor-fold desc="获取文本实际最宽数据">
    private fun StaticLayout.maxLineWidth(): Float {
        var maxWidth = 0f
        for (index in 0 until this.lineCount) {
            val w = this.getLineWidth(index)
            if (w > maxWidth) {
                maxWidth = w
            }
        }
        return maxWidth
    }
    //</editor-fold>
}