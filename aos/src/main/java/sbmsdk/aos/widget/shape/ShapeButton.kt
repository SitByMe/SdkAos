package sbmsdk.aos.widget.shape

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import sbmsdk.aos.R

/**
 * desc   :
 * date   : 2023/5/16
 * @author zoulinheng
 */
class ShapeButton(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatButton(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  private var shapeHelper: ShapeViewHelper

  init {
    val array: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShapeButton)
    shapeHelper = ShapeViewHelper(array)
    shapeHelper.setColorValue(R.styleable.ShapeButton_shape_color)
    shapeHelper.setStrokeColorValue(R.styleable.ShapeButton_shape_stroke_color)
    shapeHelper.setStrokeWidthValue(R.styleable.ShapeButton_shape_stroke_width)
    shapeHelper.setRadiusValue(R.styleable.ShapeButton_shape_radius)
    shapeHelper.setRadiusTopLeftValue(R.styleable.ShapeButton_shape_radius_top_left)
    shapeHelper.setRadiusTopRightValue(R.styleable.ShapeButton_shape_radius_top_right)
    shapeHelper.setRadiusBottomLeftValue(R.styleable.ShapeButton_shape_radius_bottom_left)
    shapeHelper.setRadiusBottomRightValue(R.styleable.ShapeButton_shape_radius_bottom_right)
    array.recycle()

    shapeHelper.createShapeDrawable()?.let { background = it }
  }
}