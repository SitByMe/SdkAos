package sbm.demo.sdk.aos.act.http.frg

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import sbmsdk.aos.createViewByLayoutId
import sbm.demo.sdk.aos.R
import sbmsdk.aos.rxhttp.download.DownloadState
import sbmsdk.aos.rxhttp.download.download
import sbmsdk.aos.setOnClickListenerProxy

/**
 * desc   :
 * date   : 2024/3/26
 * @author zoulinheng
 */
class DownloadFragment : Fragment() {
  private val mTag = this::class.java.simpleName

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return createViewByLayoutId(R.layout.fragment_download, container, false)
  }

  private lateinit var tvContent: AppCompatTextView
  private lateinit var tvState: AppCompatTextView
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val button: AppCompatButton = view.findViewById(R.id.button)
    tvContent = view.findViewById(R.id.tv_content)
    tvState = view.findViewById(R.id.tv_state)

    button.setOnClickListenerProxy {
      val url = "http://192.168.4.6/App/dm-app/2024%E5%B9%B45%E6%9C%8816%E6%97%A5/1320/%E7%82%B9%E5%90%8D%E7%B3%BB%E7%BB%9FPda_2_v1.0.2_debug_mate.apk"
      download(
        url = url,
        saveName = "mate.apk",
        subDirName = "/AAA/BBB"
      )
    }
  }

  private fun download(url: String, saveName: String? = null, subDirName: String? = null) {
    lifecycleScope.download(url = url, saveName = saveName, subDirName = subDirName)
      .progress {
        Log.w(mTag, "onProgress: $it")
        tvContent.text = it.toString()
      }.state { state ->
        when (state) {
          is DownloadState.Downloading ->
            state.message.let {
              Log.w(mTag, "state Downloading: $it")
              tvState.text = it
            }
          is DownloadState.Failed      ->
            state.message.let {
              Log.w(mTag, "state failed: $it")
              tvState.text = it
            }
          is DownloadState.None        ->
            state.message.let {
              Log.w(mTag, "state None: $it")
              tvState.text = it
            }
          is DownloadState.Succeed     ->
            state.message.let {
              Log.w(mTag, "state success: $it")
              tvState.text = it
            }
          is DownloadState.Waiting     ->
            state.message.let {
              Log.w(mTag, "state Waiting: $it")
              tvState.text = it
            }
        }
      }.start()
  }
}