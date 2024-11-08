package sbmsdk.aos.dialogx.view.binput

import android.graphics.Color
import android.text.method.DigitsKeyListener
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.KeyboardUtils
import com.kongzue.dialogx.dialogs.BottomDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.kongzue.dialogx.style.MaterialStyle
import sbmsdk.aos.buildGradientDrawable
import sbmsdk.aos.dialogx.view.IDialog
import sbmsdk.aos.ktx.R

/**
 * desc   : 底部输入型
 * date   : 2022/11/29
 *
 * @author SitByMe
 */
class BottomInputDialog(private val config: BottomInputDialogConfig) : IDialog<BottomInputDialog> {

  val value: BottomDialog by lazy {
    val dialog = BottomDialog.build()
      .setStyle(MaterialStyle.style())
      .setCustomView(object : OnBindView<BottomDialog>(R.layout.dialog_input_bottom) {
        override fun onBind(dialog: BottomDialog, v: View) {
          val tvCount = v.findViewById<AppCompatTextView>(R.id.tv_count)
          v.findViewById<AppCompatEditText>(R.id.et_input)?.let { view ->
            view.isEnabled = config.editable
            config.inputType?.let {
              view.inputType = it
            }
            config.digits?.let {
              view.keyListener = DigitsKeyListener.getInstance(it)
            }
            config.getInputFilters()?.let { filters ->
              view.filters = filters
            }
            view.background = buildGradientDrawable {
              setStroke(Color.LTGRAY, 2)
              setRadiusDimen(view.resources, sbmsdk.aos.R.dimen.dp_5)
            }
            view.addTextChangedListener {
              tvCount?.text = "${it?.length ?: 0}"
            }
            config.text?.let {
              view.setText(it)
            }
            config.hint?.let {
              view.hint = it
            }
          }
        }
      })
    //在构造如果不先调用一次show方法了的话，键盘弹出来后不会将整个dialog往上顶
    //这应该是DialogX的设计上的问题
    config.getActivity()?.let {
      dialog.show(it)
    } ?: dialog.show()
    dialog.title = config.title
    config.message?.let {
      dialog.setMessage(it)
    }
    config.okAction?.let { action ->
      dialog.setOkButton(action.text) { dialog, v ->
        val content = dialog.customView?.findViewById<AppCompatEditText>(R.id.et_input)?.text?.toString() ?: ""
        action.action?.done(this@BottomInputDialog, v, content)
        true
      }
      action.textColor?.let {
        dialog.dialogView.findViewById<TextView>(com.kongzue.dialogx.R.id.btn_selectPositive)?.setTextColor(it)
      }
    }
    config.cancelAction?.let { action ->
      dialog.setCancelButton(action.text) { dialog, v ->
        val content = dialog.customView?.findViewById<AppCompatEditText>(R.id.et_input)?.text?.toString() ?: ""
        action.action?.done(this@BottomInputDialog, v, content)
        dismiss()
        true
      }
      action.textColor?.let {
        dialog.dialogView.findViewById<TextView>(com.kongzue.dialogx.R.id.btn_selectNegative)?.setTextColor(it)
      }
    }
    config.outsideCancel?.let {
      dialog.isCancelable = it
    }
    dialog
  }

  override fun isShow(): Boolean = value.isShow

  override fun show() {
    value.isAllowInterceptTouch = false
    if (config.autoShowInputKeyboard == true)
      KeyboardUtils.showSoftInput(value.customView.findViewById<AppCompatEditText>(R.id.et_input))
  }

  override fun dismiss() {
    value.dismiss()
    value.dialogView.postDelayed({ KeyboardUtils.hideSoftInput(value.dialogView) }, 100)
  }
}