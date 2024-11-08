package sbmsdk.aos

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import sbmsdk.aos.dialogx.view.DialogState
import sbmsdk.aos.dialogx.view.binput.BottomInputDialog
import sbmsdk.aos.dialogx.view.binput.BottomInputDialogConfig
import sbmsdk.aos.dialogx.view.input.InputDialog
import sbmsdk.aos.dialogx.view.input.InputDialogConfig
import sbmsdk.aos.dialogx.view.itemselect.ItemSelectListDialog
import sbmsdk.aos.dialogx.view.itemselect.ItemSelectScrollDialog
import sbmsdk.aos.dialogx.view.loading.LoadingDialog
import sbmsdk.aos.dialogx.view.message.MessageDialog
import sbmsdk.aos.dialogx.view.message.MessageDialogConfig

/**
 * desc   : Dialog 扩展
 * date   : 2022/7/15
 *
 * @author zoulinheng
 */

/**
 * 构建输入型dialog
 *
 * @param title 标题
 * @param block 配置Config
 */
fun buildInputDialog(title: CharSequence, block: InputDialogConfig.() -> Unit): InputDialog {
  val config = InputDialogConfig(title)
  block.invoke(config)
  return InputDialog(config)
}

/**
 * 构建消息型dialog
 *
 * @param title 标题
 * @param block 配置Config
 */
fun buildMessageDialog(title: CharSequence, block: MessageDialogConfig.() -> Unit): MessageDialog {
  val config = MessageDialogConfig(title)
  block.invoke(config)
  return MessageDialog(config)
}

/**
 * 构建底部输入型dialog
 *
 * @param title 标题
 * @param block 配置Config
 */
fun buildBottomInputDialog(title: CharSequence, block: BottomInputDialogConfig.() -> Unit): BottomInputDialog {
  val config = BottomInputDialogConfig(title)
  block.invoke(config)
  return BottomInputDialog(config)
}

/**
 * 显示加载框
 */
fun showLoading(text: CharSequence? = null) {
  LoadingDialog.show(text)
}

/**
 * 显示加载框
 */
fun showLoading(activity: Activity, text: CharSequence? = null) {
  LoadingDialog.show(activity, text)
}

/**
 * 隐藏加载框
 */
fun dismissLoading() {
  LoadingDialog.dismiss()
}

/**
 * 构造一个纯列表样式的单选Dialog
 *
 * @param datas                   可供选择的数据集合
 * @param config                  配置
 * @param cancelable              点击外部是否关闭
 * @param onStateChangedListener  dialog show 和 dismiss 的状态监听器
 * @param selectBlock             选择确认回调
 */
fun <T : Any> Context.buildItemSelectListDialog(
  datas: List<T>,
  config: ItemSelectListDialog.Config<T>,
  cancelable: Boolean = false,
  onStateChangedListener: ((DialogState) -> Unit)? = null,
  selectBlock: (T) -> Unit,
): AlertDialog {
  return ItemSelectListDialog(this, config, cancelable).apply {
    setDatas(datas = datas, selectBlock = selectBlock)
    onStateChangedListener?.let { setOnStateChangedListener(it) }
  }
}

/**
 * 构造一个Wheel滚动样式的单选Dialog
 *
 * @param datas                   可供选择的数据集合
 * @param config                  配置
 * @param isCyclic                是否循环滚动
 * @param cancelable              点击外部是否关闭
 * @param openSearch              开启搜索功能
 * @param onStateChangedListener  dialog show 和 dismiss 的状态监听器
 * @param selectBlock             选择确认回调
 */
fun <T : Any> Context.buildItemSelectScrollDialog(
  datas: List<T>,
  config: ItemSelectScrollDialog.Config<T>,
  isCyclic: Boolean = false,
  cancelable: Boolean = false,
  openSearch: Boolean = false,
  onStateChangedListener: ((DialogState) -> Unit)? = null,
  selectBlock: (T) -> Unit,
): AlertDialog {
  return ItemSelectScrollDialog(this, config, cancelable).apply {
    if (openSearch) openSearch()
    setTitle(title = config.getTitle())
    setDatas(datas = datas, selectBlock = selectBlock)
    setCyclic(isCyclic = isCyclic)
    onStateChangedListener?.let { setOnStateChangedListener(it) }
  }
}