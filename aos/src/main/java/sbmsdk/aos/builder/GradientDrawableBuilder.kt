package sbmsdk.aos.builder

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi

/**
 * desc   : GradientDrawable构造器
 * date   : 2023/4/3
 *
 * @author zoulinheng
 */
class GradientDrawableBuilder {
  private val mDrawable = GradientDrawable()

  /**
   * 获取drawable
   */
  fun getDrawable(): Drawable = mDrawable

  /**
   * 设置形状
   */
  fun setShape(@Shape shape: Int): GradientDrawableBuilder {
    mDrawable.shape = shape
    return this
  }

  /**
   * 填充颜色
   */
  fun setColor(@ColorInt color: Int): GradientDrawableBuilder {
    mDrawable.setColor(color)
    return this
  }

  /**
   * 填充颜色
   */
  fun setColor(colorStateList: ColorStateList): GradientDrawableBuilder {
    mDrawable.color = colorStateList
    return this
  }

  /**
   * 填充颜色
   */
  fun setColors(orientation: Orientation, @ColorInt vararg colors: Int): GradientDrawableBuilder {
    mDrawable.orientation = orientation
    mDrawable.colors = colors.toTypedArray().toIntArray()
    return this
  }

  /**
   * 填充颜色
   */
  @RequiresApi(Build.VERSION_CODES.Q)
  fun setColors(orientation: Orientation, @ColorInt colors: IntArray, offsets: FloatArray?): GradientDrawableBuilder {
    mDrawable.orientation = orientation
    mDrawable.setColors(colors, offsets)
    return this
  }

  /**
   * 设置描边
   */
  fun setStroke(@ColorInt strokeColor: Int, strokeWidth: Int = 1): GradientDrawableBuilder {
    mDrawable.setStroke(strokeWidth, strokeColor)
    return this
  }

  /**
   * 设置圆角
   */
  fun setRadius(radius: Float): GradientDrawableBuilder {
    mDrawable.cornerRadius = radius
    return this
  }

  /**
   * 设置圆角
   */
  fun setRadiusDimen(resources: Resources, @DimenRes dimen: Int): GradientDrawableBuilder {
    setRadius(radius = resources.getDimensionPixelSize(dimen).toFloat())
    return this
  }

  /**
   * 设置圆角
   */
  fun setRadius(
    topLeft: Float = 0f,
    topRight: Float = 0f,
    bottomRight: Float = 0f,
    bottomLeft: Float = 0f,
  ): GradientDrawableBuilder {
    mDrawable.cornerRadii =
      floatArrayOf(
        topLeft,
        topLeft,
        topRight,
        topRight,
        bottomRight,
        bottomRight,
        bottomLeft,
        bottomLeft
      )
    return this
  }

  /**
   * 设置圆角
   */
  fun setRadiusDimen(
    resources: Resources,
    @DimenRes topLeftDimen: Int,
    @DimenRes topRightDimen: Int,
    @DimenRes bottomRightDimen: Int,
    @DimenRes bottomLeftDimen: Int,
  ): GradientDrawableBuilder {
    val topLeft = resources.getDimensionPixelSize(topLeftDimen).toFloat()
    val topRight = resources.getDimensionPixelSize(topRightDimen).toFloat()
    val bottomRight = resources.getDimensionPixelSize(bottomRightDimen).toFloat()
    val bottomLeft = resources.getDimensionPixelSize(bottomLeftDimen).toFloat()
    setRadius(topLeft, topRight, bottomRight, bottomLeft)
    return this
  }

  @IntDef(RECTANGLE, OVAL, LINE, RING)
  @Retention(AnnotationRetention.SOURCE)
  annotation class Shape

  companion object {
    const val RECTANGLE = GradientDrawable.RECTANGLE
    const val OVAL = GradientDrawable.OVAL
    const val LINE = GradientDrawable.LINE
    const val RING = GradientDrawable.RING
  }
}