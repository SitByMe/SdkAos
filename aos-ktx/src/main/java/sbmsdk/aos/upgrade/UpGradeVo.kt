package sbmsdk.aos.upgrade

import com.blankj.utilcode.util.AppUtils

/**
 * desc   :
 * date   : 2023/7/25
 *
 * @author zoulinheng
 *
 * @property versionCode    版本号
 * @property versionName    版本名
 * @property forceCode      强制升级code（0：非强制； 1：强制）
 * @property updateTime     版本更新时间
 * @property downloadUrl    新版本下载地址
 * @property problemFix     处理的问题描述
 */
data class UpGradeVo(
  val versionCode: Int?,
  val versionName: String?,
  private val forceCode: Int?,
  val updateTime: String?,
  val downloadUrl: String?,
  val problemFix: String?,
) {

  /**
   * 是否需要升级
   */
  fun needUpgrade(): Boolean {
    versionCode ?: return false
    return versionCode > AppUtils.getAppVersionCode()
  }

  /**
   * 是否强制升级
   *
   * 只要[forceCode]不为0就是强制升级
   */
  fun isForceUpgrade(): Boolean {
    return forceCode != 0
  }
}