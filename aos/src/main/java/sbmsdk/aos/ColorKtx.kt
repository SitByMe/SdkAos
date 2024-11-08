package sbmsdk.aos

import android.content.res.ColorStateList
import sbmsdk.aos.builder.ColorStateListBuilder

/**
 * desc   :
 * date   : 2023/4/4
 *
 * @author zoulinheng
 */

/**
 * 构造 ColorStateList
 */
fun buildColorStateList(scope: ColorStateListBuilder.() -> Unit): ColorStateList {
  val builder = ColorStateListBuilder()
  scope.invoke(builder)
  return builder.build().getColor()
}