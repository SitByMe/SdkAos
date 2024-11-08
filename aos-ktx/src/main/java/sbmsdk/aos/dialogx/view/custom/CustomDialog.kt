package sbmsdk.aos.dialogx.view.custom

import android.app.Activity
import android.view.View
import androidx.annotation.LayoutRes
import com.kongzue.dialogx.interfaces.OnBindView
import sbmsdk.aos.dialogx.view.IDialog
import com.kongzue.dialogx.dialogs.CustomDialog as CustomDialogX

/**
 * desc   :
 * date   : 2023/5/17
 *
 * @author SitByMe
 */
abstract class CustomDialog(@LayoutRes layoutResId: Int) : IDialog<CustomDialog> {
  val value: CustomDialogX by lazy {
    val dialog = CustomDialogX.build()
    dialog.setCustomView(object : OnBindView<CustomDialogX>(layoutResId) {
      override fun onBind(dialog: CustomDialogX, v: View) {
        initView(v)
      }
    })
    dialog
  }
  private var activity: Activity? = null

  abstract fun initView(view: View)

  fun bindActivity(activity: Activity?): CustomDialog {
    this.activity = activity
    return this
  }

  override fun show() {
    activity?.let {
      value.show(it)
    } ?: value.show()
  }

  override fun dismiss() {
    value.dismiss()
  }

  override fun isShow(): Boolean {
    return value.isShow
  }
}