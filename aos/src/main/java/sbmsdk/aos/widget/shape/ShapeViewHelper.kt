package sbmsdk.aos.widget.shape

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes
import sbmsdk.aos.buildGradientDrawable

/**
 * desc   : ShapeView的辅助类
 * date   : 2023/7/11
 * @author zoulinheng
 */
class ShapeViewHelper(private val array: TypedArray) {

  private var hasShape = false

  @ColorInt
  private var color: Int? = null

  @ColorInt
  private var strokeColor: Int? = null
  private var strokeWidth: Int? = null
  private var radius: Int? = null
  private var radiusTopLeft: Int? = null
  private var radiusTopRight: Int? = null
  private var radiusBottomLeft: Int? = null
  private var radiusBottomRight: Int? = null

  fun setColorValue(@StyleableRes index: Int) {
    if (array.containsValue(index)) {
      this.color = array.getColor(index, Color.TRANSPARENT)
    }
  }

  fun setStrokeColorValue(@StyleableRes index: Int) {
    if (array.containsValue(index)) {
      this.strokeColor = array.getColor(index, Color.TRANSPARENT)
    }
  }

  fun setStrokeWidthValue(@StyleableRes index: Int) {
    if (array.containsValue(index)) {
      this.strokeWidth = array.getDimensionPixelSize(index, 0)
    }
  }

  fun setRadiusValue(@StyleableRes index: Int) {
    if (array.containsValue(index)) {
      this.radius = array.getDimensionPixelSize(index, 0)
    }
  }

  fun setRadiusTopLeftValue(@StyleableRes index: Int) {
    if (array.containsValue(index)) {
      this.radiusTopLeft = array.getDimensionPixelSize(index, 0)
    }
  }

  fun setRadiusTopRightValue(@StyleableRes index: Int) {
    if (array.containsValue(index)) {
      this.radiusTopRight = array.getDimensionPixelSize(index, 0)
    }
  }

  fun setRadiusBottomLeftValue(@StyleableRes index: Int) {
    if (array.containsValue(index)) {
      this.radiusBottomLeft = array.getDimensionPixelSize(index, 0)
    }
  }

  fun setRadiusBottomRightValue(@StyleableRes index: Int) {
    if (array.containsValue(index)) {
      this.radiusBottomRight = array.getDimensionPixelSize(index, 0)
    }
  }

  /**
   * 创建 Shape drawable
   */
  fun createShapeDrawable(): Drawable? {
    if (!hasShape) return null
    return buildGradientDrawable {
      color?.let { setColor(it) }
      strokeColor?.let {
        setStroke(strokeColor = it, strokeWidth = strokeWidth ?: 1)
      }
      radius?.let { setRadius(radius = it.toFloat()) }
      ?: kotlin.run {
        if (radiusTopLeft != null ||
            radiusTopRight != null ||
            radiusBottomLeft != null ||
            radiusBottomRight != null
        ) {
          setRadius(
            topLeft = radiusTopLeft?.toFloat() ?: 0f,
            topRight = radiusTopRight?.toFloat() ?: 0f,
            bottomRight = radiusBottomRight?.toFloat() ?: 0f,
            bottomLeft = radiusBottomLeft?.toFloat() ?: 0f
          )
        }
      }
    }
  }

  private fun TypedArray.containsValue(@StyleableRes index: Int): Boolean {
    if (hasValue(index)) {
      hasShape = true
      return true
    }
    return false
  }
}