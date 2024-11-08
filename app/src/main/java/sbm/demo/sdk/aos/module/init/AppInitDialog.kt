package sbm.demo.sdk.aos.module.init

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.kongzue.dialogx.dialogs.FullScreenDialog
import com.kongzue.dialogx.interfaces.OnBindView
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.module.init.widget.SAppInitLayout
import sbmsdk.aos.setOnClickListenerProxy
import sbmsdk.aos.showAppToastShort
import sbmsdk.aos.dialogx.view.IDialog

/**
 * desc   :
 * date   : 2024/7/3
 *
 * @author zoulinheng
 */
class AppInitDialog private constructor(private val appInitDataModel: AppInitDataModel) : IDialog<AppInitDialog> {

  private val dialog = FullScreenDialog.build(object : OnBindView<FullScreenDialog>(R.layout.aos_dialog_app_init) {
    override fun onBind(dialog: FullScreenDialog, v: View) {
      v.findViewById<SAppInitLayout>(R.id.ai_layout)?.setData(appInitDataModel)

      v.findViewById<AppCompatButton>(R.id.btn_submit)?.setOnClickListenerProxy { _ ->
        appInitDataModel.submit().let {
          if (it.isSuccess) {
            showAppToastShort(it.getOrNull() ?: "保存成功")
            dismiss()
          } else {
            showAppToastShort(it.exceptionOrNull()?.message ?: "保存失败")
          }
        }
      }
      v.findViewById<AppCompatTextView>(R.id.tv_back)?.setOnClickListenerProxy {
        dismiss()
      }
      v.findViewById<AppCompatButton>(R.id.btn_cancel)?.setOnClickListenerProxy {
        dismiss()
      }
    }
  })

  companion object {
    fun show(appInitDataModel: AppInitDataModel) {
      return AppInitDialog(appInitDataModel).show()
    }
  }

  override fun isShow(): Boolean {
    return dialog.isShow
  }

  override fun show() {
    dialog.show()
  }

  override fun dismiss() {
    dialog.dismiss()
  }
}