package sbmsdk.aos.rxhttp.download

/**
 * desc   :
 * date   : 2024/5/8
 *
 * @author zoulinheng
 */
sealed class MultiDownloadState {
  class None : MultiDownloadState() {
    override val message: String = "未开始"
  }

  class Waiting : MultiDownloadState() {
    override val message: String = "等待中"
  }

  class Downloading : MultiDownloadState() {
    override val message: String = "下载中"
  }

  class Completed(val fTasks: List<DownloadTask2>, val sTasks: List<DownloadTask2>) : MultiDownloadState() {
    override val message: String = "已完成"
  }

  abstract val message: String

  /**
   * 判断是否是同一个类型
   */
  internal fun isSameType(other: MultiDownloadState): Boolean {
    return getType() == other.getType()
  }

  private fun getType(): Int {
    //这几个值只要不一样就行
    return when (this) {
      is Completed   -> 1
      is Downloading -> 2
      is None        -> 3
      is Waiting     -> 4
    }
  }
}