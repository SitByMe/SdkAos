package sbmsdk.aos.helper

import com.blankj.utilcode.util.PermissionUtils
import sbmsdk.aos.showAppToastLong

/**
 * desc   : 请求权限辅助类
 * date   : 2024/2/6
 *
 * @author zoulinheng
 *
 * @param permissions 需要请求的权限
 */
class PermissionHelper(permissions: Array<String>) {

  //已在manifest中注册的权限
  private val registerList = mutableListOf<String>()

  //未在manifest中注册的权限
  private val unRegisterList = mutableListOf<String>()

  init {
    val allPers = PermissionUtils.getPermissions()
    permissions.onEach {
      if (allPers.contains(it)) {
        registerList.add(it)
      } else {
        unRegisterList.add(it)
      }
    }
  }

  private var isCyclic = false

  /**
   * 是否在请求失败后自动重新发起请求
   *
   * 只在[failedCallback]为null的时候生效
   */
  fun isCyclic(cyclic: Boolean): PermissionHelper {
    this.isCyclic = cyclic
    return this
  }

  //权限请求失败的回调
  private var failedCallback: FailedCallback? = null

  fun setFailedCallback(callback: FailedCallback): PermissionHelper {
    this.failedCallback = callback
    return this
  }

  /**
   * 发起请求
   *
   * @param successCallback 请求成功的回调
   */
  fun request(successCallback: () -> Unit) {
    PermissionUtils.permission(*registerList.toTypedArray())
      .callback { isAllGranted, granted, deniedForever, denied ->
        if (isAllGranted) {
          successCallback.invoke()
        } else {
          if (isCyclic) {
            request(successCallback)
          } else {
            (failedCallback ?: defFailedCallback).callback(
              helper = this@PermissionHelper,
              granted = granted,
              deniedForever = deniedForever,
              denied = denied,
              unRegister = unRegisterList
            )
          }
        }
      }.request()
  }

  interface FailedCallback {
    /**
     * @param helper          当前对象
     * @param granted         以获取的权限
     * @param deniedForever   拒绝并不在提醒的权限
     * @param denied          拒绝的权限
     * @param unRegister      未在manifest.xml中注册的权限
     */
    fun callback(helper: PermissionHelper, granted: List<String>, deniedForever: List<String>, denied: List<String>, unRegister: List<String>)
  }

  private val defFailedCallback: FailedCallback = object : FailedCallback {
    override fun callback(helper: PermissionHelper, granted: List<String>, deniedForever: List<String>, denied: List<String>, unRegister: List<String>) {
      showAppToastLong("有权限获取失败，请到应用设置中进行操作")
    }
  }
}