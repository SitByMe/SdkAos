package sbmsdk.aos.widget.shape

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import sbmsdk.aos.R

/**
 * desc   :
 * date   : 2023/7/11
 * @author zoulinheng
 */
class ShapeView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayoutCompat(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  private var shapeHelper: ShapeViewHelper

  init {
    val array: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShapeView)
    shapeHelper = ShapeViewHelper(array)
    shapeHelper.setColorValue(R.styleable.ShapeView_shape_color)
    shapeHelper.setStrokeColorValue(R.styleable.ShapeView_shape_stroke_color)
    shapeHelper.setStrokeWidthValue(R.styleable.ShapeView_shape_stroke_width)
    shapeHelper.setRadiusValue(R.styleable.ShapeView_shape_radius)
    shapeHelper.setRadiusTopLeftValue(R.styleable.ShapeView_shape_radius_top_left)
    shapeHelper.setRadiusTopRightValue(R.styleable.ShapeView_shape_radius_top_right)
    shapeHelper.setRadiusBottomLeftValue(R.styleable.ShapeView_shape_radius_bottom_left)
    shapeHelper.setRadiusBottomRightValue(R.styleable.ShapeView_shape_radius_bottom_right)
    array.recycle()

    shapeHelper.createShapeDrawable()?.let { background = it }
  }
}