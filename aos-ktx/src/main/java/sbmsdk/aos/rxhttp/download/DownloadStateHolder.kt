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
internal class DownloadStateHolder {
  private var stateJob: Job? = null
  private val flow = MutableStateFlow<DownloadState>(DownloadState.None())

  /**
   * 设置下载状态监听器
   */
  fun addStateListener(launchIn: CoroutineScope, listener: DownloadStateListener) {
    stateJob?.cancel()
    stateJob = launchIn.launch {
      this@DownloadStateHolder.flow.collect {
        listener.onState(it)
      }
    }
    //设置监听器的同时，回调一次当前状态
    this.flow.value = this.flow.value
  }

  fun updateState(state: DownloadState) {
    if (flow.value.isSameType(state)) return
    flow.value = state
  }
}