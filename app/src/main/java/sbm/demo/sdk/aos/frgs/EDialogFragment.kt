package sbm.demo.sdk.aos.frgs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sbmsdk.aos.*
import sbmsdk.aos.action.ActionType
import sbmsdk.aos.debug.view.addButtonGroup
import sbmsdk.aos.debug.view.addTitle
import sbm.demo.sdk.aos.R
import sbmsdk.aos.dialogx.view.action.MessageDialogAction
import sbmsdk.aos.dialogx.view.itemselect.ItemSelectListDialog
import sbmsdk.aos.dialogx.view.itemselect.ItemSelectScrollDialog
import sbmsdk.aos.dialogx.view.listener.OnMessageDialogActionListener
import sbmsdk.aos.dialogx.view.loading.LoadingDialog
import sbmsdk.aos.dialogx.view.message.MessageDialog

/**
 * desc   :
 * date   : 2024/9/11
 *
 * @author zoulinheng
 */
class EDialogFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return createViewByLayoutId(R.layout.fragment_simple_scrollable_ll, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.findViewById<LinearLayoutCompat>(R.id.ll_content).apply {
      addTitle("输入型")
      addButtonGroup {
        add("输入型dialog（1）", inputDialogClick1)
        add("输入型dialog（2）", inputDialogClick2)
        add("底部输入型dialog（1）", bottomInputDialogClick1)
        add("底部输入型dialog（2）", bottomInputDialogClick2)
        add("底部输入型dialog（3）", bottomInputDialogClick3)
      }
      addTitle("消息型")
      addButtonGroup {
        add("消息型dialog（1）", messageDialogClick1)
        add("消息型dialog（2）", messageDialogClick2)
        add("消息型dialog（3）", messageDialogClick3)
      }
      addTitle("列表选项")
      addButtonGroup {
        add("列表样式Dialog", openItemSelectListDialog)
        add("滚动样式Dialog", openItemSelectScrollDialog)
      }
      addTitle("加载框")
      addButtonGroup {
        add("普通弹出", loadingNormal)
        add("连续弹出多个", loadingDialog)
        add("加载进度", loadingDownloadProcess)
      }
    }
  }

  private val inputDialogClick1 = OnClickListener {
    buildInputDialog("输入型dialog") {
      message = "只有一个按钮"
      outsideCancel = true
      autoShowInputKeyboard = false
      okAction = buildOkAction {
        this.dismiss()
        showToastShort(it)
      }
    }.show()
  }

  private val inputDialogClick2 = OnClickListener {
    buildInputDialog("输入型dialog") {
      message = "有两个按钮"
      okAction = buildOkAction {
        this.dismiss()
        showToastShort(it)
      }
      cancelAction = defCancelAction()
    }.show()
  }

  private val messageDialogClick1 = OnClickListener {
    buildMessageDialog("消息型dialog") {
      message = "只有一个按钮"
      okAction = buildOkAction {
        this.dismiss()
        showToastShort("确定")
      }
    }.show()
  }

  private val messageDialogClick2 = OnClickListener {
    buildMessageDialog("消息型dialog") {
      message = "有两个按钮"
      okAction = buildOkAction {
        this.dismiss()
        showToastShort("确定")
      }
      cancelAction = defCancelAction()
    }.show()
  }

  private val messageDialogClick3 = OnClickListener {
    buildMessageDialog("消息型dialog") {
      message = "有三个按钮"
      okAction = buildOkAction {
        this.dismiss()
        showToastShort("确定")
      }
      cancelAction = defCancelAction()
      otherAction = MessageDialogAction(
        text = "其他",
        actionType = ActionType.NORMAL,
        action = object : OnMessageDialogActionListener<MessageDialog> {
          override fun done(dialog: MessageDialog, view: View) {
            dialog.dismiss()
            showToastShort("其他")
          }
        })
    }.show()
  }

  private val bottomInputDialogClick1 = OnClickListener {
    buildBottomInputDialog("底部输入型dialog") {
      message = "只有一个按钮"
      outsideCancel = false
      okAction = buildOkAction {
        this.dismiss()
        showToastShort(it)
      }
    }.show()
  }

  private val bottomInputDialogClick2 = OnClickListener {
    buildBottomInputDialog("底部输入型dialog") {
      message = "有两个按钮"
      outsideCancel = true
      autoShowInputKeyboard = true
      okAction = buildOkAction {
        this.dismiss()
        showToastShort(it)
      }
      cancelAction = defCancelAction()
    }.show()
  }

  private val bottomInputDialogClick3 = OnClickListener {
    buildBottomInputDialog("底部输入型dialog") {
      message = "不可编辑"
      text = "这一段文字是不可编辑的"
      editable = false
      outsideCancel = true
      autoShowInputKeyboard = true
      okAction = buildOkAction {
        this.dismiss()
        showToastShort(it)
      }
      cancelAction = defCancelAction()
    }.show()
  }

  private val openItemSelectListDialog = OnClickListener {
    requireContext().buildItemSelectListDialog(
      datas = listOf("选项一", "选项二"),
      config = object : ItemSelectListDialog.Config<String> {
        override fun getText(t: String): CharSequence = t
      }) {
      showToastShort(it)
    }.show()
  }

  private val openItemSelectScrollDialog = OnClickListener {
    requireContext().buildItemSelectScrollDialog(
      datas = (1 until 21).map { "选项 $it" },
      config = object : ItemSelectScrollDialog.Config<String> {
        override fun getText(t: String): CharSequence {
          return t
        }
      },
      openSearch = true
    ) {
      showToastShort(it)
    }.show()
  }

  private val loadingNormal = OnClickListener {
    lifecycleScope.launch {
      showLoading("3秒后自动关闭...")
      delay(3000)
      dismissLoading()
    }
  }

  private val loadingDialog = OnClickListener {
    lifecycleScope.launch {
      LoadingDialog.show()
      delay(2000)
      LoadingDialog.show("再来一发")
      delay(2000)
      LoadingDialog.show("第三个发送")
      delay(2000)
      LoadingDialog.show("下一次就取消")
      delay(2000)
      LoadingDialog.dismiss()
      delay(2000)
      showToastShort("再多取消一次")
      LoadingDialog.dismiss()
    }
  }

  private val loadingDownloadProcess = OnClickListener {
    val maxValue = 2000
    lifecycleScope.launch {
      repeat(maxValue) {
        showLoading(it.toString())
        delay(2)
      }
      showLoading("${maxValue}\n3秒后自动关闭...")
      delay(3000)
      dismissLoading()
    }
  }
}