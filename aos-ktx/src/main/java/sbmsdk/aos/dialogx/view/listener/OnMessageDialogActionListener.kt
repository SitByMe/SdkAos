package sbmsdk.aos.dialogx.view.listener

import android.view.View
import sbmsdk.aos.dialogx.view.IDialog

/**
 * desc   : 消息型dialog按钮行为监听事件
 * date   : 2022/11/29
 *
 * @author SitByMe
 */
interface OnMessageDialogActionListener<D : IDialog<D>> {

  fun done(dialog: D, view: View)
}