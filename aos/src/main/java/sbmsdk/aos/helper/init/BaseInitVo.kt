package sbmsdk.aos.helper.init

import sbmsdk.aos.callback.SCallback1F
import sbmsdk.aos.callback.SimpleFailedStatus
import sbmsdk.aos.helper.PermissionHelper

/**
 * desc   :
 * date   : 2024/10/23
 *
 * @author zoulinheng
 */
abstract class BaseInitVo<CB : Any>(protected val passed: ((CB) -> Unit)? = null) {
  abstract fun init(callback: SCallback1F<String, SimpleFailedStatus>)
}

class PermissionsInitVo(private val permissions: Array<String>, passed: ((String) -> Unit)? = null) : BaseInitVo<String>(passed) {
  override fun init(callback: SCallback1F<String, SimpleFailedStatus>) {
    PermissionHelper(permissions)
      .isCyclic(true)
      .request {
        passed?.invoke("已获取所有应用权限")
        callback.callback("已获取所有应用权限")
      }
  }
}