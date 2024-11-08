package sbmsdk.aos.dialogx.view.loading

import android.app.Activity
import android.graphics.Color
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ThreadUtils
import com.kongzue.dialogx.dialogs.WaitDialog
import sbmsdk.aos.dialogx.view.IDialog

/**
 * desc   : 加载框
 * date   : 2023/7/6
 *
 * @author zoulinheng
 */
object LoadingDialog : IDialog<LoadingDialog> {

  private var closeFlag = true

  override fun isShow(): Boolean {
    return !closeFlag
  }

  fun show(text: CharSequence? = null) {
    ThreadUtils.runOnUiThread {
      WaitDialog.show(text).inlineShow()
    }
  }

  fun show(activity: Activity, text: CharSequence? = null) {
    ThreadUtils.runOnUiThread {
      WaitDialog.show(activity, text).inlineShow()
    }
  }

  /*fun show(progress: Float) {
    ThreadUtils.runOnUiThread {
      WaitDialog.show(progress).inlineShow()
    }
  }

  fun show(activity: Activity, progress: Float) {
    ThreadUtils.runOnUiThread {
      WaitDialog.show(activity, progress).inlineShow()
    }
  }*/

  fun show(activity: Activity) {
    show(activity, "加载中...")
  }

  override fun show() {
    show("加载中...")
  }

  override fun dismiss() {
    closeFlag = true
    WaitDialog.dismiss()
  }

  @MainThread
  private fun WaitDialog.inlineShow() {
    closeFlag = false
    this.setOnBackPressedListener {
      dismiss()
      false
    }.apply {
      this.dialogView?.findViewById<TextView>(com.kongzue.dialogx.R.id.txt_info)?.let {
        it.setTextColor(ContextCompat.getColor(it.context, com.kongzue.dialogx.R.color.colorAccent))
      }
      this.dialogImpl?.blurView?.setOverlayColor(Color.WHITE)
      //      this.dialogImpl?.progressView?.setColor(Color.BLACK)
    }
  }
}