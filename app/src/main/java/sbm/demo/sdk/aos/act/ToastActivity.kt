package sbm.demo.sdk.aos.act

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.databinding.ActivityToastBinding
import sbm.demo.sdk.aos.lib.ToastHelper
import sbmsdk.aos.showToastShort

/**
 * desc   :
 * date   : 2024/2/19
 * @author zoulinheng
 */
class ToastActivity : AppCompatActivity() {

  private lateinit var binding: ActivityToastBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_toast)
    binding.view = this
  }

  val showToaster = View.OnClickListener {
    ToastHelper().show("custom dialog")
  }

  val showAosToast = View.OnClickListener {
    showToastShort("aos toast")
  }
}