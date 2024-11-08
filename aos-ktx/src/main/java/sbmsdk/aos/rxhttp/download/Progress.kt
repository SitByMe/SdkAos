package sbmsdk.aos.rxhttp.download

/**
 * desc   : 进度
 * date   : 2022/10/31
 *
 * @author zoulinheng
 *
 * @property progress     进度（0-100）
 * @property currentSize  当前大小
 * @property totalSize    总大小
 */
data class Progress(
  private val progress: Int,
  val currentSize: Long,
  val totalSize: Long,
) {

  fun getProgress(): Double {
    return progress.toDouble()
  }

  override fun toString(): String {
    return "Progress(progress=$progress, currentSize=$currentSize, totalSize=$totalSize)"
  }
}
