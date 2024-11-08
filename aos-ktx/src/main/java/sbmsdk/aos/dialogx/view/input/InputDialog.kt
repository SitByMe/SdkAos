package sbmsdk.aos.dialogx.view.input

import android.text.method.DigitsKeyListener
import android.widget.EditText
import android.widget.TextView
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback
import sbmsdk.aos.dialogx.view.IDialog
import sbmsdk.aos.setTextSizeDimen
import com.kongzue.dialogx.dialogs.InputDialog as InputDialogX

/**
 * desc   : 输入型
 * date   : 2022/11/29
 *
 * @author SitByMe
 */
class InputDialog(config: InputDialogConfig) : IDialog<InputDialog> {

  val value: InputDialogX by lazy {
    val dialog = InputDialogX.build()
    dialog.dialogLifecycleCallback = object : DialogLifecycleCallback<MessageDialog>() {
      override fun onShow(dialog: MessageDialog) {
        super.onShow(dialog)
        val etView = dialog.dialogView.findViewById<EditText>(com.kongzue.dialogx.R.id.txt_input)
        etView.isEnabled = config.editable
        config.inputType?.let {
          etView.inputType = it
        }
        config.digits?.let {
          etView.keyListener = DigitsKeyListener.getInstance(it)
        }
        config.getInputFilters()?.let { filters ->
          etView.filters = filters
        }
      }
    }
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
      dialog.setOkButton(it.text) { _, v ->
        val content = dialog.inputText
        it.action?.done(this@InputDialog, v, content)
        true
      }
      it.textColor?.let { tc ->
        dialog.dialogView.findViewById<TextView>(com.kongzue.dialogx.R.id.btn_selectPositive)?.run {
          setTextColor(tc)
          setTextSizeDimen(sbmsdk.aos.R.dimen.sp_14)
        }
      }
    }
    config.cancelAction?.let {
      dialog.setCancelButton(it.text) { _, v ->
        val content = dialog.inputText
        it.action?.done(this@InputDialog, v, content)
        dismiss()
        true
      }
      it.textColor?.let { tc ->
        dialog.dialogView.findViewById<TextView>(com.kongzue.dialogx.R.id.btn_selectNegative)?.run {
          setTextColor(tc)
          setTextSizeDimen(sbmsdk.aos.R.dimen.sp_14)
        }
      }
    }
    config.outsideCancel?.let {
      dialog.isCancelable = it
    }
    config.hint?.let {
      dialog.inputHintText = it
    }
    config.text?.let {
      dialog.inputText = it
    }
    config.autoShowInputKeyboard?.let {
      dialog.isAutoShowInputKeyboard = it
    }
    dialog
  }

  override fun isShow(): Boolean = value.isShow

  override fun show() {
    value
  }

  override fun dismiss() {
    value.dismiss()
  }
}