package sbmsdk.aos.rxhttp.download

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * desc   :
 * date   : 2024/3/26
 *
 * @author zoulinheng
 */
internal class DownloadProgressHolder {
  private var stateJob: Job? = null
  private val flow = MutableStateFlow<Progress?>(null)

  /**
   * 设置下载进度监听器
   */
  fun addProgressListener(launchIn: CoroutineScope, listener: ProgressListener) {
    stateJob?.cancel()
    stateJob = launchIn.launch {
      this@DownloadProgressHolder.flow.collect {
        it ?: return@collect
        listener.onProgress(it)
      }
    }
    //设置监听器的同时，回调一次当前进度
    this.flow.value = this.flow.value
  }

  fun updateProgress(progress: Progress) {
    flow.value = progress
  }
}