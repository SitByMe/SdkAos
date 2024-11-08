package sbmsdk.aos.widget.shape

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.RelativeLayout
import sbmsdk.aos.R

/**
 * desc   :
 * date   : 2024/2/2
 * @author zoulinheng
 */
class ShapeRelativeLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  private var shapeHelper: ShapeViewHelper

  init {
    val array: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShapeRelativeLayout)
    shapeHelper = ShapeViewHelper(array)
    shapeHelper.setColorValue(R.styleable.ShapeRelativeLayout_shape_color)
    shapeHelper.setStrokeColorValue(R.styleable.ShapeRelativeLayout_shape_stroke_color)
    shapeHelper.setStrokeWidthValue(R.styleable.ShapeRelativeLayout_shape_stroke_width)
    shapeHelper.setRadiusValue(R.styleable.ShapeRelativeLayout_shape_radius)
    shapeHelper.setRadiusTopLeftValue(R.styleable.ShapeRelativeLayout_shape_radius_top_left)
    shapeHelper.setRadiusTopRightValue(R.styleable.ShapeRelativeLayout_shape_radius_top_right)
    shapeHelper.setRadiusBottomLeftValue(R.styleable.ShapeRelativeLayout_shape_radius_bottom_left)
    shapeHelper.setRadiusBottomRightValue(R.styleable.ShapeRelativeLayout_shape_radius_bottom_right)
    array.recycle()

    shapeHelper.createShapeDrawable()?.let { background = it }
  }
}