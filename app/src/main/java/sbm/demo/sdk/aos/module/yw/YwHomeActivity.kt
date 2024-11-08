package sbm.demo.sdk.aos.module.yw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import sbmsdk.aos.buildMessageDialog
import sbmsdk.aos.debug.view.addButton
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.act.http.HttpActivity
import sbmsdk.aos.showToastLong
import sbmsdk.aos.startAct
import sbmsdk.aos.upgrade.CheckResult
import sbmsdk.aos.upgrade.UpGradeVo
import sbmsdk.aos.upgrade.UpgradeHelper

/**
 * desc   :
 * date   : 2024/4/29
 * @author zoulinheng
 */
class YwHomeActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_simple_scrollable_ll)

    findViewById<LinearLayoutCompat>(R.id.ll_content)?.let { view ->
      view.addButton("应用升级") { checkUpgrade() }
      view.addButton("附件") { startAct<HttpActivity> { } }
    }
  }

  private fun checkUpgrade() {
    val upgradeVo = UpGradeVo(
      versionCode = 100, versionName = "1.0.0", forceCode = 0, downloadUrl = "http://192.168.4.6/App/dm-app/2024%E5%B9%B44%E6%9C%8828%E6%97%A5/1755/app-debug-%E4%BC%98%E5%8D%9A%E8%AE%AF.apk", updateTime = null, problemFix = null
    )
    UpgradeHelper {
      when (this) {
        is CheckResult.Failure -> {
          buildMessageDialog("更新失败") {
            this.message = this@UpgradeHelper.message.ifEmpty {
              "出现异常，请联系管理员"
            }
            this.okAction = buildOkAction { this.dismiss() }
          }.show()
        }
        is CheckResult.Pass    -> {
          showToastLong("检查通过，进入App")
        }
      }
    }.checkUpgrade(upgradeVo)
  }
}