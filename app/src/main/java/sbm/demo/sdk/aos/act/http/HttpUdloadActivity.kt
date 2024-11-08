package sbm.demo.sdk.aos.act.http

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.act.base.openFragment
import sbm.demo.sdk.aos.act.http.frg.DownloadFragment
import sbm.demo.sdk.aos.act.http.frg.DownloadMultiFragment

/**
 * desc   :
 * date   : 2024/3/1
 * @author zoulinheng
 */
class HttpUdloadActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_http_udload)
  }

  /**
   * 单个下载
   */
  fun download(view: View) {
    openFragment<DownloadFragment>()
  }

  /**
   * 批量下载
   */
  fun downloadMulti(view: View) {
    openFragment<DownloadMultiFragment>()
  }
}