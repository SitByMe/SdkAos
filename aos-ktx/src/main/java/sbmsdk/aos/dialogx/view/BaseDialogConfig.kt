package sbmsdk.aos.dialogx.view

import android.app.Activity
import android.text.InputFilter
import android.view.View
import sbmsdk.aos.dialogx.view.action.InputCancelDialogAction
import sbmsdk.aos.dialogx.view.action.InputConfirmDialogAction
import sbmsdk.aos.dialogx.view.action.InputDialogAction
import sbmsdk.aos.dialogx.view.listener.OnInputDialogActionListener

/**
 * desc   :
 * date   : 2023/5/17
 *
 * @author SitByMe
 */
open class BaseDialogConfig<D : IDialog<D>>(val title: CharSequence) {
  private var activity: Activity? = null
  var message: CharSequence? = null
  var outsideCancel: Boolean? = null

  fun bindActivity(activity: Activity?) {
    this.activity = activity
  }

  fun getActivity(): Activity? {
    return activity
  }
}

open class BaseInputDialogConfig<D : IDialog<D>>(title: CharSequence) : BaseDialogConfig<D>(title) {
  var hint: String? = null
  var text: String? = null
  var editable: Boolean = true
  var autoShowInputKeyboard: Boolean? = null

  /*---------- input filters ----------*/
  //最大可输入长度
  private var maxCount: Int? = null

  /**
   * 设置最大可输入长度
   */
  fun setMaxLength(maxLength: Int?) {
    maxCount = if (maxLength == null || maxLength <= 0) {
      null
    } else {
      maxLength
    }
  }

  var digits: String? = null

  private var inputFilters: Array<InputFilter>? = null

  fun setInputFilters(filters: Array<InputFilter>?) {
    this.inputFilters = filters
  }

  fun getInputFilters(): Array<InputFilter>? {
    val maxCountFilter = maxCount?.let {
      InputFilter.LengthFilter(it)
    }
    return if (maxCountFilter == null) {
      inputFilters
    } else {
      val fs = inputFilters?.toMutableList()
      fs?.apply {
        this.filterIsInstance<InputFilter.LengthFilter>().let {
          if (it.isNotEmpty()) {
            fs.removeAll(it)
          }
        }
      }
      (fs ?: mutableListOf()).apply {
        this.add(maxCountFilter)
      }.toTypedArray()
    }
  }

  var inputType: Int? = null

  /*---------- action ----------*/
  var okAction: InputDialogAction<D>? = null
  var cancelAction: InputDialogAction<D>? = null

  /*---------- simple creator ----------*/

  fun buildOkAction(text: CharSequence? = null, block: D.(String?) -> Unit): InputConfirmDialogAction<D> {
    return InputConfirmDialogAction(
      text = text,
      action = object : OnInputDialogActionListener<D> {
        override fun done(dialog: D, view: View, text: String?) {
          block.invoke(dialog, text)
        }
      })
  }

  fun buildCancelAction(text: CharSequence? = null, block: D.(View) -> Unit): InputCancelDialogAction<D> {
    return InputCancelDialogAction(
      text = text,
      action = object : OnInputDialogActionListener<D> {
        override fun done(dialog: D, view: View, text: String?) {
          block.invoke(dialog, view)
        }
      })
  }

  fun defCancelAction(text: CharSequence? = null): InputCancelDialogAction<D> {
    return buildCancelAction(text) {
      this.dismiss()
    }
  }
}