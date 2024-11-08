package sbmsdk.aos.widget.shape

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import sbmsdk.aos.R
import sbmsdk.aos.widget.text.DrawableTextView

/**
 * desc   :
 * date   : 2023/7/17
 * @author zoulinheng
 */
class ShapeDrawableTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : DrawableTextView(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  private var shapeHelper: ShapeViewHelper

  init {
    val array: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShapeDrawableTextView)
    shapeHelper = ShapeViewHelper(array)
    shapeHelper.setColorValue(R.styleable.ShapeDrawableTextView_shape_color)
    shapeHelper.setStrokeColorValue(R.styleable.ShapeDrawableTextView_shape_stroke_color)
    shapeHelper.setStrokeWidthValue(R.styleable.ShapeDrawableTextView_shape_stroke_width)
    shapeHelper.setRadiusValue(R.styleable.ShapeDrawableTextView_shape_radius)
    shapeHelper.setRadiusTopLeftValue(R.styleable.ShapeDrawableTextView_shape_radius_top_left)
    shapeHelper.setRadiusTopRightValue(R.styleable.ShapeDrawableTextView_shape_radius_top_right)
    shapeHelper.setRadiusBottomLeftValue(R.styleable.ShapeDrawableTextView_shape_radius_bottom_left)
    shapeHelper.setRadiusBottomRightValue(R.styleable.ShapeDrawableTextView_shape_radius_bottom_right)
    array.recycle()

    shapeHelper.createShapeDrawable()?.let { background = it }
  }
}