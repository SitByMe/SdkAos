package sbmsdk.aos.rxhttp.download

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore

/**
 * desc   :
 * date   : 2022/10/31
 *
 * @author zoulinheng
 */
object DownloadManager {

  /**
   * 下载多个任务
   *
   * @param scope
   * @param tasks                 一组下载任务
   * @param permits               可同时下载的数量
   * @param stateListener         下载状态监听器
   * @param progressListener      下载进度监听器
   * @param taskStateListener     单个下载任务的状态监听器
   * @param taskProgressListener  单个下载任务的进度监听器
   */
  fun downloadTasks(
    scope: CoroutineScope = MainScope(),
    tasks: List<DownloadTask2>,
    permits: Int = 3,
    stateListener: MultiDownloadStateListener? = null,
    progressListener: MultiProgressListener? = null,
    taskStateListener: DownloadTaskStateListener? = null,
    taskProgressListener: TaskProgressListener? = null,
  ) {
    val totalCount = tasks.size

    val fDatas = mutableListOf<DownloadTask2>()
    val sDatas = mutableListOf<DownloadTask2>()

    stateListener?.onState(MultiDownloadState.Waiting())

    val semaphore = Semaphore(permits.coerceAtLeast(1))
    scope.launch {
      stateListener?.onState(MultiDownloadState.Downloading())
      progressListener?.onProgress(MultiProgress(0, 0, totalCount))
      tasks.onEach { t ->
        semaphore.acquire()
        t.progress {
          taskProgressListener?.onProgress(t, it)
        }.state {
          taskStateListener?.onState(t, it)
          when (it) {
            is DownloadState.Downloading -> Unit
            is DownloadState.Failed      -> fDatas.add(t)
            is DownloadState.None        -> Unit
            is DownloadState.Succeed     -> sDatas.add(t)
            is DownloadState.Waiting     -> Unit
          }
          if (it is DownloadState.Failed || it is DownloadState.Succeed) {
            semaphore.release()
            progressListener?.onProgress(MultiProgress(fDatas.size, sDatas.size, totalCount))
            if (fDatas.size + sDatas.size == tasks.size) {
              stateListener?.onState(MultiDownloadState.Completed(fTasks = fDatas, sTasks = sDatas))
            }
          }
        }.start()
      }
    }
  }
}