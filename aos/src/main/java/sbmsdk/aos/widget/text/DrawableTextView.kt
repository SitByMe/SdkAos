package sbmsdk.aos.widget.text

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import sbmsdk.aos.R

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2021/04/18
 *    desc   : 支持限定 Drawable 大小的 TextView
 *    如果 drawableSize 的值不大于0，则宽高分别以 drawableWidth 和 drawableHeight 为准
 */
open class DrawableTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatTextView(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  private var drawableWidth: Int
  private var drawableHeight: Int

  init {
    val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView)
    val drawableSize = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableSize, 0)
    drawableWidth = if (drawableSize <= 0) array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableWidth, 0) else drawableSize
    drawableHeight = if (drawableSize <= 0) array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableHeight, 0) else drawableSize
    array.recycle()
    refreshDrawablesSize()
  }

  /**
   * 限定 Drawable 大小
   */
  fun setDrawableSize(width: Int, height: Int) {
    drawableWidth = width
    drawableHeight = height
    if (!isAttachedToWindow) {
      return
    }
    refreshDrawablesSize()
  }

  /**
   * 限定 Drawable 宽度
   */
  fun setDrawableWidth(width: Int) {
    drawableWidth = width
    if (!isAttachedToWindow) {
      return
    }
    refreshDrawablesSize()
  }

  /**
   * 限定 Drawable 高度
   */
  fun setDrawableHeight(height: Int) {
    drawableHeight = height
    if (!isAttachedToWindow) {
      return
    }
    refreshDrawablesSize()
  }

  override fun setCompoundDrawables(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
    super.setCompoundDrawables(left, top, right, bottom)
    if (!isAttachedToWindow) {
      return
    }
    refreshDrawablesSize()
  }

  override fun setCompoundDrawablesRelative(start: Drawable?, top: Drawable?, end: Drawable?, bottom: Drawable?) {
    super.setCompoundDrawablesRelative(start, top, end, bottom)
    if (!isAttachedToWindow) {
      return
    }
    refreshDrawablesSize()
  }

  /**
   * 刷新 Drawable 列表大小
   */
  private fun refreshDrawablesSize() {
    if (drawableWidth == 0 || drawableHeight == 0) {
      return
    }
    var compoundDrawables: Array<Drawable?> = compoundDrawables
    if (compoundDrawables[0] != null || compoundDrawables[1] != null) {
      super.setCompoundDrawables(
        limitDrawableSize(compoundDrawables[0]), limitDrawableSize(compoundDrawables[1]),
        limitDrawableSize(compoundDrawables[2]), limitDrawableSize(compoundDrawables[3])
      )
      return
    }
    compoundDrawables = compoundDrawablesRelative
    super.setCompoundDrawablesRelative(
      limitDrawableSize(compoundDrawables[0]), limitDrawableSize(compoundDrawables[1]),
      limitDrawableSize(compoundDrawables[2]), limitDrawableSize(compoundDrawables[3])
    )
  }

  /**
   * 重新限定 Drawable 宽高
   */
  private fun limitDrawableSize(drawable: Drawable?): Drawable? {
    if (drawable == null) {
      return null
    }
    if (drawableWidth == 0 || drawableHeight == 0) {
      return drawable
    }
    drawable.setBounds(0, 0, drawableWidth, drawableHeight)
    return drawable
  }
}