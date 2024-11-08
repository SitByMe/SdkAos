package sbm.demo.sdk.aos.ui

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import sbmsdk.aos.callback.SCallback1F
import sbmsdk.aos.callback.SimpleFailedStatus
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.databinding.ActivityMainBinding
import sbm.demo.sdk.aos.init.AppInitor
import sbm.demo.sdk.aos.module.widget.builder
import sbmsdk.aos.showToastLong

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.view = this

    binding.tabLayout.let {
      //      it.setAutoFullTabMode()
      it.builder()
        .addPage("Aos", AosFragment())
        .addPage("Me", MeFragment())
        .build(supportFragmentManager, binding.viewPager, lifecycle)
    }

    AppInitor.init(lifecycleScope, object : SCallback1F<String, SimpleFailedStatus> {
      override fun callback(var1: String) {
        "App初始化成功".let {
          AppInitor.logInit(it)
          showToastLong(it)
        }
      }

      override fun failed(status: SimpleFailedStatus) {
        "App初始化失败：${status.message}".let {
          AppInitor.logInit(it)
          showToastLong(it)
        }
      }
    })
  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    return super.onTouchEvent(event)
  }
}