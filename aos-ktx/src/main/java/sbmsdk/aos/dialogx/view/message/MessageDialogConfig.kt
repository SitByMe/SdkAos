package sbmsdk.aos.dialogx.view.message

import android.view.View
import sbmsdk.aos.dialogx.view.BaseDialogConfig
import sbmsdk.aos.dialogx.view.action.MessageCancelDialogAction
import sbmsdk.aos.dialogx.view.action.MessageConfirmDialogAction
import sbmsdk.aos.dialogx.view.action.MessageDialogAction
import sbmsdk.aos.dialogx.view.listener.OnMessageDialogActionListener

/**
 * desc   :
 * date   : 2023/5/17
 * @author SitByMe
 */
class MessageDialogConfig(title: CharSequence) : BaseDialogConfig<MessageDialog>(title) {

  /*---------- action ----------*/
  var okAction: MessageDialogAction<MessageDialog>? = null
  var cancelAction: MessageDialogAction<MessageDialog>? = null
  var otherAction: MessageDialogAction<MessageDialog>? = null


  /*---------- simple creator ----------*/

  fun buildOkAction(text: CharSequence? = null, block: MessageDialog.(View) -> Unit): MessageDialogAction<MessageDialog> {
    return MessageConfirmDialogAction(
      text = text,
      action = object : OnMessageDialogActionListener<MessageDialog> {
        override fun done(dialog: MessageDialog, view: View) {
          block.invoke(dialog, view)
        }
      })
  }

  fun buildCancelAction(text: CharSequence? = null, block: MessageDialog.(View) -> Unit): MessageDialogAction<MessageDialog> {
    return MessageCancelDialogAction(
      text = text,
      action = object : OnMessageDialogActionListener<MessageDialog> {
        override fun done(dialog: MessageDialog, view: View) {
          block.invoke(dialog, view)
        }
      })
  }

  fun defCancelAction(text: CharSequence? = null): MessageDialogAction<MessageDialog> {
    return buildCancelAction(text) {
      this.dismiss()
    }
  }
}