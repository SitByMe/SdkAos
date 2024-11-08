package sbmsdk.aos.helper

import sbmsdk.aos.buildMessageDialog
import sbmsdk.aos.callback.SCallback1F
import sbmsdk.aos.callback.SimpleFailedStatus
import sbmsdk.aos.helper.init.AppInitHelper
import sbmsdk.aos.helper.init.BaseInitVo
import sbmsdk.aos.upgrade.CheckResult
import sbmsdk.aos.upgrade.UpGradeVo
import sbmsdk.aos.upgrade.UpgradeHelper

/**
 * desc   :
 * date   : 2024/10/23
 *
 * @author zoulinheng
 */

fun AppInitHelper.InitVosBuilder.addUpgrade(creator: () -> UpgradeInitVo) {
  add(creator)
}

class UpgradeInitVo(private val upgradeVo: UpGradeVo?, private val auto: Boolean = false, passed: ((String) -> Unit)? = null) : BaseInitVo<String>(passed) {
  override fun init(callback: SCallback1F<String, SimpleFailedStatus>) {
    if (upgradeVo == null) {
      "App升级检测通过：未获取到升级信息".let {
        passed?.invoke(it)
        callback.callback(it)
      }
      return
    }
    UpgradeHelper {
      when (this) {
        is CheckResult.Failure -> {
          buildMessageDialog("更新失败") {
            val msg = this@UpgradeHelper.message.ifEmpty {
              "出现异常，请联系管理员"
            }
            this.message = msg
            this.okAction = buildOkAction {
              callback.failed(SimpleFailedStatus(msg))
              this.dismiss()
            }
          }.show()
        }
        is CheckResult.Pass    -> {
          "App升级检测通过：不需要升级".let {
            passed?.invoke(it)
            callback.callback(it)
          }
        }
      }
    }.checkUpgrade(upgradeVo, auto)
  }
}