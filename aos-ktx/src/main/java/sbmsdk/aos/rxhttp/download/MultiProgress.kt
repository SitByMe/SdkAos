package sbmsdk.aos.rxhttp.download

/**
 * desc   : 多任务进度
 * date   : 2024/5/8
 *
 * @author zoulinheng
 *
 * @property fCount     失败数
 * @property sCount     成功数
 * @property totalCount 总数
 */
data class MultiProgress(
  val fCount: Int,
  val sCount: Int,
  val totalCount: Int,
) {

  override fun toString(): String {
    return "MultiProgress(fCount=$fCount, sCount=$sCount, totalCount=$totalCount)"
  }
}
