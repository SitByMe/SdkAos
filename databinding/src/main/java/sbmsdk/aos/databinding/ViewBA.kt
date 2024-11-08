package sbmsdk.aos.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import sbmsdk.aos.databinding.ktx.setOnClickListenerProxy

/**
 * @author zoulinheng
 * date  : 2021/3/5
 * desc  : 普通 View 相关 BindingAdapter
 */
object ViewBA {

  /**
   * View 的防重点击操作
   *
   * @param onClickListener listener
   * @param clickInterval   间隔时长（毫秒）
   */
  @JvmStatic
  @BindingAdapter(value = ["click", "clickInterval"], requireAll = false)
  fun setOnClickListenerProxy(view: View, onClickListener: View.OnClickListener?, clickInterval: Long?) {
    if (onClickListener == null) {
      view.setOnClickListener(null)
    } else {
      view.setOnClickListenerProxy(clickInterval, onClickListener)
    }
  }

  /**
   * view 的显示隐藏
   *
   * @param isGone 是否 gone
   */
  @JvmStatic
  @BindingAdapter("isGone")
  fun isGone(view: View, isGone: Boolean) {
    if (isGone) {
      view.visibility = View.GONE
    } else {
      view.visibility = View.VISIBLE
    }
  }

  /**
   * view 的显示隐藏
   *
   * @param inVisible 是否 invisible
   */
  @JvmStatic
  @BindingAdapter("isInvisible")
  fun isInvisible(view: View, inVisible: Boolean) {
    if (inVisible) {
      view.visibility = View.INVISIBLE
    } else {
      view.visibility = View.VISIBLE
    }
  }
}