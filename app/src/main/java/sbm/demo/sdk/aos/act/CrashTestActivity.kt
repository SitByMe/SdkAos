package sbm.demo.sdk.aos.act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import sbm.demo.sdk.aos.R
import sbmsdk.aos.setOnClickListenerProxy

/**
 * desc   :
 * date   : 2024/2/20
 * @author zoulinheng
 */
class CrashTestActivity : AppCompatActivity() {

  private val btnTest: AppCompatButton by lazy { findViewById(R.id.btn_test) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_crash_test)

    btnTest.setOnClickListenerProxy {
      Thread {
        btnTest.text = "崩溃"
      }.start()
    }
  }
}