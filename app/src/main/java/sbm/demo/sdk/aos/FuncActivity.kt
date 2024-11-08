package sbm.demo.sdk.aos

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import sbmsdk.aos.registerStartActivityForResult
import sbmsdk.aos.setOnClickListenerProxy
import kotlin.concurrent.thread

/**
 * desc   :
 * date   : 2024/3/5
 * @author zoulinheng
 */
class FuncActivity : AppCompatActivity() {

  private val barcodeLauncher = registerStartActivityForResult {
  }

  private val semaphore = Semaphore(3)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_func)
  }

  fun semaphore1Click(view: View) {
    semaphore1()
  }

  fun semaphore2Click(view: View) {
    semaphore2()
  }

  fun semaphore1A2Click(view: View) {
    semaphore1()
    semaphore2()
  }

  private fun semaphore1() {
    thread {
      for (i in 1..10) {
        lifecycleScope.launch {
          delay(1000)
          semaphore.acquire()
          Log.w("semaphoreTest", "click1：$i")
          delay(3000)
          Log.w("semaphoreTest", "end1：$i")
          semaphore.release()
        }
      }
    }
  }

  private fun semaphore2() {
    thread {
      for (i in 1..10) {
        lifecycleScope.launch {
          delay(1000)
          semaphore.acquire()
          Log.w("semaphoreTest", "click2：$i")
          delay(3000)
          Log.w("semaphoreTest", "end2：$i")
          semaphore.release()
        }
      }
    }
  }
}