package sbm.demo.sdk.aos.act.http

import android.os.Bundle
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.databinding.ActivityHttpBinding
import sbmsdk.aos.startAct

/**
 * desc   :
 * date   : 2024/2/19
 * @author zoulinheng
 */
class HttpActivity : AppCompatActivity() {

  private lateinit var binding: ActivityHttpBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_http)
    binding.view = this
  }

  val upDownloadClick = OnClickListener {
    startAct<HttpUdloadActivity> { }
  }
}