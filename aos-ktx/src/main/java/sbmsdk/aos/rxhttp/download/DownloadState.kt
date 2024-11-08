package sbmsdk.aos.rxhttp.download

import sbmsdk.aos.ifNullOrEmpty

/**
 * desc   :
 * date   : 2024/3/26
 *
 * @author zoulinheng
 */
sealed class DownloadState {
  class None : DownloadState() {
    override val message: String = "未开始"
  }

  class Waiting : DownloadState() {
    override val message: String = "等待中"
  }

  class Downloading : DownloadState() {
    override val message: String = "下载中"
  }

  //  class Stopped : DownloadState()
  class Failed(message: String?) : DownloadState() {
    override val message: String = message.ifNullOrEmpty { "下载出现异常" }
  }

  class Succeed(val destPath: String) : DownloadState() {
    override val message: String = destPath
  }

  abstract val message: String

  /**
   * 判断是否是同一个类型
   */
  internal fun isSameType(other: DownloadState): Boolean {
    return getType() == other.getType()
  }

  private fun getType(): Int {
    //这几个值只要不一样就行
    return when (this) {
      is Downloading -> 1
      is Failed      -> 2
      is None        -> 3
      is Succeed     -> 4
      is Waiting     -> 5
    }
  }
}