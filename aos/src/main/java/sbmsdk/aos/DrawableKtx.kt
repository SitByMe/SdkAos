package sbmsdk.aos

import android.graphics.drawable.Drawable
import sbmsdk.aos.builder.GradientDrawableBuilder
import sbmsdk.aos.builder.StateListDrawableBuilder

/**
 * desc   :
 * date   : 2023/4/3
 *
 * @author zoulinheng
 */

fun buildGradientDrawable(scope: GradientDrawableBuilder.() -> Unit): Drawable {
  val builder = GradientDrawableBuilder()
  scope.invoke(builder)
  return builder.getDrawable()
}

fun buildStateListDrawable(scope: StateListDrawableBuilder.() -> Unit): Drawable {
  val builder = StateListDrawableBuilder()
  scope.invoke(builder)
  return builder.build().getDrawable()
}