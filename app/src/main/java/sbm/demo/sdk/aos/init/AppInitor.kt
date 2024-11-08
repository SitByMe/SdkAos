package sbm.demo.sdk.aos.init

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import sbmsdk.aos.callback.SCallback1F
import sbmsdk.aos.callback.SimpleFailedStatus
import sbmsdk.aos.helper.init.AppInitHelper
import sbmsdk.aos.helper.init.BaseInitVo
import sbmsdk.aos.helper.init.PermissionsInitVo
import sbmsdk.aos.upgrade.UpGradeVo

/**
 * desc   :
 * date   : 2024/10/23
 *
 * @author zoulinheng
 */
object AppInitor {

  private var upgradeInfo: UpGradeVo? = null

  private val permissions = arrayListOf(
    android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    android.Manifest.permission.READ_PHONE_STATE)

  fun init(scope: CoroutineScope, callback: SCallback1F<String, SimpleFailedStatus>) {
    AppInitHelper.build {
      add {
        object : BaseInitVo<Boolean>(passed = {
          logInit("自定义初始化成功")
        }) {
          override fun init(callback: SCallback1F<String, SimpleFailedStatus>) {
            if (true) {
              passed?.invoke(true)
              callback.callback("custom success")
            } else {
              callback.failed(SimpleFailedStatus("custom failed"))
            }
          }
        }
      }
      addPermissions { PermissionsInitVo(permissions.toTypedArray()) { logInit(it) } }
      /*addApollo {
        ApolloInitVo(scope = scope, clazz = ApolloConfig::class.java, appId = "ejy.aos", nameSpace = "FaceCollectServer") {
          upgradeInfo = UpGradeVo(versionCode = 100, versionName = "1.0.2", forceCode = 1, updateTime = "2024年10月23日17:36:21", downloadUrl = "", problemFix = "修复了一些内容")
          logInit("获取Apollo数据成功：${it.toJson()}")
        }
      }
      addUpgrade {
        UpgradeInitVo(upgradeInfo) { logInit(it) }
      }*/
    }.init(
      object : SCallback1F<String, SimpleFailedStatus> {
        override fun callback(var1: String) {
          callback.callback(var1)
        }

        override fun failed(status: SimpleFailedStatus) {
          callback.failed(status)
        }
      })
  }

  fun logInit(text: String) {
    Log.w("AppInit", text)
  }
}