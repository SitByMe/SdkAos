package sbmsdk.aos.upgrade

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.IntentUtils
import sbmsdk.aos.buildMessageDialog
import sbmsdk.aos.dialogx.view.action.MessageCancelDialogAction
import sbmsdk.aos.dialogx.view.action.MessageConfirmDialogAction
import sbmsdk.aos.dialogx.view.listener.OnMessageDialogActionListener
import sbmsdk.aos.dialogx.view.loading.LoadingDialog
import sbmsdk.aos.dialogx.view.message.MessageDialog
import sbmsdk.aos.rxhttp.download.DownloadState
import sbmsdk.aos.rxhttp.download.DownloadTask2
import sbmsdk.aos.topActivityOrNull

/**
 * desc   :
 * date   : 2023/7/25
 *
 * @author zoulinheng
 *
 * @param resultBlock 结果回调
 */
class UpgradeHelper(private val installLauncher: ActivityResultLauncher<Intent>? = null, private val resultBlock: CheckResult.() -> Unit) {

  private var upgradeDialog: MessageDialog? = null

  /**
   * 检查升级信息
   *
   * @param auto  校验通过后是否自动开始升级逻辑
   */
  fun checkUpgrade(upGradeVo: UpGradeVo, auto: Boolean = false) {
    if (upGradeVo.needUpgrade()) {
      if (auto) {
        startUpgrade(upGradeVo)
      } else {
        val isForce = upGradeVo.isForceUpgrade()
        upgradeDialog?.dismiss()
        upgradeDialog = buildMessageDialog("有新版本") {
          bindActivity(topActivityOrNull)
          //        message = upGradeVo.problemFix
          message = "点击确定按钮开始升级"
          okAction = MessageConfirmDialogAction(action = object : OnMessageDialogActionListener<MessageDialog> {
            override fun done(dialog: MessageDialog, view: View) {
              dialog.dismiss()
              startUpgrade(upGradeVo)
            }
          })
          outsideCancel = !isForce
          if (!isForce) {
            cancelAction = MessageCancelDialogAction(action = object : OnMessageDialogActionListener<MessageDialog> {
              override fun done(dialog: MessageDialog, view: View) {
                dialog.dismiss()
              }
            })
          }
        }
        upgradeDialog?.show()
      }
    } else {
      resultBlock.invoke(CheckResult.Pass())
    }
  }

  //开始升级逻辑
  private fun startUpgrade(upGradeVo: UpGradeVo) {
    val downloadUrl = upGradeVo.downloadUrl
    if (downloadUrl.isNullOrBlank()) {
      resultBlock.invoke(CheckResult.Failure(upGradeVo.isForceUpgrade(), "新版本下载路径为空"))
      return
    }
    DownloadTask2(
      url = downloadUrl,
      subDirName = "新版本"
    ).state {
      when (it) {
        is DownloadState.Downloading -> Unit
        is DownloadState.Failed      -> {
          LoadingDialog.dismiss()
          resultBlock.invoke(CheckResult.Failure(upGradeVo.isForceUpgrade(), it.message))
        }
        is DownloadState.None        -> Unit
        is DownloadState.Succeed     -> {
          LoadingDialog.show("准备安装")
          //调用安装
          installLauncher?.launch(IntentUtils.getInstallAppIntent(it.destPath)) ?: AppUtils.installApp(it.destPath)
        }
        is DownloadState.Waiting     -> Unit
      }
    }.progress {
      LoadingDialog.show("下载中...${it.getProgress()}%")
    }.start()
  }
}

sealed class CheckResult {
  //通过检查
  class Pass : CheckResult()

  //检查失败
  class Failure(val isForceUpgrade: Boolean, val message: String) : CheckResult()
}