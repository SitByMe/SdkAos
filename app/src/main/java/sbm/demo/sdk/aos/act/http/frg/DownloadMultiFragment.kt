package sbm.demo.sdk.aos.act.http.frg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import sbmsdk.aos.buildMessageDialog
import sbmsdk.aos.createViewByLayoutId
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.test.TestData
import sbmsdk.aos.rxhttp.download.*
import sbmsdk.aos.setOnClickListenerProxy

/**
 * desc   : 批量下载
 * date   : 2024/3/22
 * @author zoulinheng
 */
class DownloadMultiFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return createViewByLayoutId(R.layout.fragment_download_logs_of, container, false)
  }

  private lateinit var tvTitle: AppCompatTextView
  private lateinit var recyclerView: RecyclerView
  private lateinit var btnStart: AppCompatButton

  private val successTasks = mutableSetOf<DownloadTask2>()
  private val failedTasks = mutableSetOf<DownloadTask2>()

  private val titleFlow = MutableStateFlow<CharSequence>("等待开始")

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    tvTitle = view.findViewById(R.id.tv_title)
    recyclerView = view.findViewById(R.id.recycler_view)
    btnStart = view.findViewById(R.id.btn_start)

    lifecycleScope.launch {
      titleFlow.collect {
        tvTitle.text = it
      }
    }

    btnStart.setOnClickListenerProxy {
      download(tasks)
    }

    view.findViewById<View>(R.id.btn_show_lose).setOnClickListenerProxy {
      buildMessageDialog("丢失的数据") {
        message = buildString {
          failedTasks.onEach {
            if (this.isNotEmpty()) {
              append("\n")
            }
            append(it)
          }
        }
        okAction = buildOkAction("重新下载") {
          download(failedTasks.toList())
        }
      }.show()
    }
  }

  private fun download(tasks: List<DownloadTask2>) {
    DownloadManager.downloadTasks(
      scope = lifecycleScope,
      tasks = tasks,
      stateListener = object : MultiDownloadStateListener {
        override fun onState(state: MultiDownloadState) {
          when (state) {
            is MultiDownloadState.Completed   -> {
              showTitle("全部下载完成：失败 ${state.fTasks.size} 个，成功 ${state.sTasks.size} 个，总共 ${tasks.size} 个")
              failedTasks.addAll(state.fTasks)
              successTasks.addAll(state.sTasks)
            }
            is MultiDownloadState.Downloading -> Unit
            is MultiDownloadState.None        -> Unit
            is MultiDownloadState.Waiting     -> showTitle("准备开始下载...")
          }
        }
      },
      progressListener = object : MultiProgressListener {
        override fun onProgress(p: MultiProgress) {
          showTitle("下载进度：失败 ${p.fCount} 个，成功 ${p.sCount} 个，总共 ${tasks.size} 个")
        }
      }
    )
  }

  private val tasks =
    TestData().zfIds5000.map {
      DownloadTask2(
        url = "http://192.168.4.8:1280/DownLoad.ashx?id=&AdjunctType=cs&AdjunctFileName=${it}.jpeg&isComputer=1",
        saveName = "$it.jpeg"
      )
    }

  private fun showTitle(text: CharSequence) {
    titleFlow.value = text
  }
}
