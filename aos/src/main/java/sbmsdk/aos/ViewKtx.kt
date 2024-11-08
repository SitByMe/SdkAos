package sbmsdk.aos

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver
import com.blankj.utilcode.util.SizeUtils

/**
 * desc   :
 * date   : 2022/8/17
 *
 * @author zoulinheng
 */

fun View.visible() {
  visibility = View.VISIBLE
}

fun View.gone() {
  visibility = View.GONE
}

fun View.invisible() {
  visibility = View.INVISIBLE
}

/**
 * 为[this]增加 margin 值
 */
fun View.addMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
  if (left == 0 && top == 0 && right == 0 && bottom == 0) return
  val layoutParams = this.layoutParams as? MarginLayoutParams
  layoutParams?.setMargins(
    layoutParams.leftMargin + left,
    layoutParams.topMargin + top,
    layoutParams.rightMargin + right,
    layoutParams.bottomMargin + bottom
  )
}

/**
 * 为[this]的 marginTop 值增加一个状态栏的高度
 */
fun View.addMarginTopEqualStatusBarHeight() {
  addMargin(0, getStatusBarHeight(), 0, 0)
}

/**
 * 为[this]设置 margin 值
 */
fun View.setMargin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
  if (left == null && top == null && right == null && bottom == null) return
  val layoutParams = this.layoutParams as? MarginLayoutParams
  layoutParams?.setMargins(
    left?.coerceAtLeast(0) ?: layoutParams.leftMargin,
    top?.coerceAtLeast(0) ?: layoutParams.topMargin,
    right?.coerceAtLeast(0) ?: layoutParams.rightMargin,
    bottom?.coerceAtLeast(0) ?: layoutParams.bottomMargin
  )
}

/**
 * [View] 扩展函数
 * 函数名：[setMaxHeight] 设置最大高度
 * @param maxHeight 控件新高度
 */
fun View.setMaxHeight(maxHeight: Number) {
  this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
    override fun onGlobalLayout() {
      val currentHeight = this@setMaxHeight.height
      if (currentHeight != 0) {
        if (currentHeight > maxHeight.toInt()) {
          this@setMaxHeight.setHeight(maxHeight)
          this@setMaxHeight.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
      }
    }
  })
}

/**
 * [View] 扩展函数
 * 函数名：[setHeight] 设置高度
 * @param height 控件新高度
 */
fun View.setHeight(height: Number) {
  val l = layoutParams ?: return kotlin.run {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        if (height != 0) {
          viewTreeObserver.removeOnGlobalLayoutListener(this)
          val l = layoutParams
          l.height = SizeUtils.dp2px(56.toFloat())
          layoutParams = l
        }
      }
    })
  }
  l.height = height.toInt()
  layoutParams = l
}

/**
 * [View] 扩展函数
 * 函数名：[setWidth] 设置宽度
 * @param width 控件新宽度
 */
fun View.setWidth(width: Number) {
  val l = layoutParams ?: return kotlin.run {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        if (width != 0) {
          viewTreeObserver.removeOnGlobalLayoutListener(this)
          val l = layoutParams
          l.height = SizeUtils.dp2px(56.toFloat())
          layoutParams = l
        }
      }
    })
  }
  l.width = width.toInt()
  layoutParams = l
}