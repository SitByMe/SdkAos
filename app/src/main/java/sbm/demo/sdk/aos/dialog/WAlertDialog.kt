package sbm.demo.sdk.aos.dialog

import android.content.Context
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog

/**
 * desc   : 网威 AlertDialog
 * date   : 2022/7/15
 * @author zoulinheng
 *
 * 1、支持监听 Dialog 的 show 和 dismiss 方法
 */
open class WAlertDialog : AlertDialog {
  constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId)
  constructor(context: Context, cancelable: Boolean = false) : super(context, cancelable, null)

  private var onStateChangedListener: ((DialogState) -> Unit)? = null

  /**
   * 设置状态改变的监听器
   */
  fun setOnStateChangedListener(onStateChangedListener: (DialogState) -> Unit) {
    this.onStateChangedListener = onStateChangedListener
  }

  override fun show() {
    super.show()
    onStateChangedListener?.invoke(DialogState.SHOWING)
  }

  override fun dismiss() {
    super.dismiss()
    onStateChangedListener?.invoke(DialogState.DISMISS)
  }
}