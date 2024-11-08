package sbmsdk.aos.dialogx.view.action

import androidx.annotation.ColorInt
import sbmsdk.aos.action.ActionType
import sbmsdk.aos.dialogx.view.IDialog
import sbmsdk.aos.dialogx.view.listener.OnInputDialogActionListener

/**
 * desc   : InputDialog 按钮行为
 * date   : 2022/11/28
 *
 * @author SitByMe
 *
 * @property text   文字
 * @property action 行为（点击事件）
 */
open class InputDialogAction<D : IDialog<D>>(
  text: CharSequence,
  @ColorInt textColor: Int? = null,
  val action: OnInputDialogActionListener<D>? = null,
) : BaseDialogAction(text = text, textColor = textColor) {
  constructor(text: CharSequence, actionType: ActionType, action: OnInputDialogActionListener<D>) :
      this(text = text, textColor = actionType.mainColor, action = action)
}

/**
 * 输入型action - 取消
 */
class InputCancelDialogAction<D : IDialog<D>>(text: CharSequence? = null, action: OnInputDialogActionListener<D>) :
  InputDialogAction<D>(text ?: "取消", actionType = ActionType.NORMAL, action = action)

/**
 * 输入型action - 确定
 */
class InputConfirmDialogAction<D : IDialog<D>>(text: CharSequence? = null, action: OnInputDialogActionListener<D>) :
  InputDialogAction<D>(text ?: "确定", actionType = ActionType.POSITIVE, action = action)