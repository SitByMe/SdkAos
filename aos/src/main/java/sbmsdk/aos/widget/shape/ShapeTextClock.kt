package sbmsdk.aos.widget.shape

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import sbmsdk.aos.R
import sbmsdk.aos.widget.text.STextClock

/**
 * desc   :
 * date   : 2023/12/12
 * @author zoulinheng
 */
class ShapeTextClock(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : STextClock(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  private var shapeHelper: ShapeViewHelper

  init {
    val array: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShapeTextClock)
    shapeHelper = ShapeViewHelper(array)
    shapeHelper.setColorValue(R.styleable.ShapeTextClock_shape_color)
    shapeHelper.setStrokeColorValue(R.styleable.ShapeTextClock_shape_stroke_color)
    shapeHelper.setStrokeWidthValue(R.styleable.ShapeTextClock_shape_stroke_width)
    shapeHelper.setRadiusValue(R.styleable.ShapeTextClock_shape_radius)
    shapeHelper.setRadiusTopLeftValue(R.styleable.ShapeTextClock_shape_radius_top_left)
    shapeHelper.setRadiusTopRightValue(R.styleable.ShapeTextClock_shape_radius_top_right)
    shapeHelper.setRadiusBottomLeftValue(R.styleable.ShapeTextClock_shape_radius_bottom_left)
    shapeHelper.setRadiusBottomRightValue(R.styleable.ShapeTextClock_shape_radius_bottom_right)
    array.recycle()

    shapeHelper.createShapeDrawable()?.let { background = it }
  }
}