package sbmsdk.aos.dialogx.view.action

import androidx.annotation.ColorInt
import sbmsdk.aos.action.ActionType
import sbmsdk.aos.dialogx.view.IDialog
import sbmsdk.aos.dialogx.view.listener.OnMessageDialogActionListener

/**
 * desc   : InputDialog 按钮行为
 * date   : 2022/11/28
 *
 * @author SitByMe
 *
 * @property text   文字
 * @property action 行为（点击事件）
 */
open class MessageDialogAction<D : IDialog<D>>(
  text: CharSequence,
  @ColorInt textColor: Int? = null,
  val action: OnMessageDialogActionListener<D>? = null,
) : BaseDialogAction(text = text, textColor = textColor) {
  constructor(text: CharSequence, actionType: ActionType, action: OnMessageDialogActionListener<D>) :
      this(text = text, textColor = actionType.mainColor, action = action)
}

/**
 * 消息型action - 取消
 */
class MessageCancelDialogAction<D : IDialog<D>>(text: CharSequence? = null, action: OnMessageDialogActionListener<D>) :
  MessageDialogAction<D>(text ?: "取消", actionType = ActionType.NORMAL, action = action)

/**
 * 消息型action - 确定
 */
class MessageConfirmDialogAction<D : IDialog<D>>(text: CharSequence? = null, action: OnMessageDialogActionListener<D>) :
  MessageDialogAction<D>(text ?: "确定", actionType = ActionType.POSITIVE, action = action)