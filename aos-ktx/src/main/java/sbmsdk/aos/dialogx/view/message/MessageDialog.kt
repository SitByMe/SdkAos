package sbmsdk.aos.dialogx.view.message

import android.widget.TextView
import com.kongzue.dialogx.util.TextInfo
import sbmsdk.aos.action.ActionType
import sbmsdk.aos.dialogx.view.IDialog
import sbmsdk.aos.dialogx.view.action.MessageDialogAction
import sbmsdk.aos.setTextSizeDimen
import com.kongzue.dialogx.dialogs.MessageDialog as MessageDialogX

/**
 * desc   : 消息型
 * date   : 2022/11/29
 *
 * @author SitByMe
 */
class MessageDialog(config: MessageDialogConfig) : IDialog<MessageDialog> {

  val value: MessageDialogX by lazy {
    val dialog = MessageDialogX.build()
    //在构造如果不先调用一次show方法了的话，键盘弹出来后不会将整个dialog往上顶
    //这应该是DialogX的bug
    config.getActivity()?.let {
      dialog.show(it)
    } ?: dialog.show()
    dialog.title = config.title
    config.message?.let {
      dialog.setMessage(it)
    }
    config.okAction?.let {
      dialog.setOkButtonAction(it)
    }
    config.cancelAction?.let {
      dialog.setCancelButtonAction(it)
    }
    config.otherAction?.let {
      dialog.setOtherButtonAction(it)
    }
    config.outsideCancel?.let {
      dialog.isCancelable = it
    }
    dialog
  }

  private fun MessageDialogX.setOkButtonAction(okButtonAction: MessageDialogAction<MessageDialog>): MessageDialogX {
    okButtonAction.let {
      this.setOkButton(it.text) { _, v ->
        it.action?.done(this@MessageDialog, v)
        true
      }
      it.textColor?.let { tc ->
        this.dialogView.findViewById<TextView>(com.kongzue.dialogx.R.id.btn_selectPositive)?.run {
          setTextColor(tc)
          setTextSizeDimen(sbmsdk.aos.R.dimen.sp_14)
        }
      }
    }
    return this
  }

  private fun MessageDialogX.setCancelButtonAction(cancelButtonAction: MessageDialogAction<MessageDialog>): MessageDialogX {
    cancelButtonAction.let {
      this.cancelTextInfo = (this.cancelTextInfo ?: TextInfo().setBold(false)).apply { fontColor = ActionType.NEGATIVE.mainColor }
      this.setCancelButton(it.text) { _, v ->
        it.action?.done(this@MessageDialog, v)
        dismiss()
        true
      }
      it.textColor?.let { tc ->
        this.dialogView.findViewById<TextView>(com.kongzue.dialogx.R.id.btn_selectNegative)?.run {
          setTextColor(tc)
          setTextSizeDimen(sbmsdk.aos.R.dimen.sp_14)
        }
      }
    }
    return this
  }

  private fun MessageDialogX.setOtherButtonAction(otherButtonAction: MessageDialogAction<MessageDialog>): MessageDialogX {
    otherButtonAction.let {
      this.setOtherButton(it.text) { _, v ->
        it.action?.done(this@MessageDialog, v)
        true
      }
      it.textColor?.let { tc ->
        this.dialogView.findViewById<TextView>(com.kongzue.dialogx.R.id.btn_selectOther)?.run {
          setTextColor(tc)
          setTextSizeDimen(sbmsdk.aos.R.dimen.sp_14)
        }
      }
    }
    return this
  }

  override fun isShow(): Boolean {
    return value.isShow
  }

  override fun show() {
    value.show()
  }

  override fun dismiss() {
    value.dismiss()
  }
}