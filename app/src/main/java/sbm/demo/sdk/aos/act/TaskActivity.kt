package sbm.demo.sdk.aos.act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import sbm.demo.sdk.aos.R
import sbmsdk.aos.setOnClickListenerProxy
import sbmsdk.aos.task.CountDownTask
import java.util.*

/**
 * desc   :
 * date   : 2024/3/8
 * @author zoulinheng
 */
class TaskActivity : AppCompatActivity() {

  private val btnCountDownTask: AppCompatButton by lazy { findViewById(R.id.btn_countdown) }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_task)

    btnCountDownTask.setOnClickListenerProxy {
      countDownTask.start(Calendar.getInstance().timeInMillis + 10000)
    }
  }

  private val countDownTask = CountDownTask(this, object : CountDownTask.CountDownCallback {
    override fun remainTime(time: Long) {
      btnCountDownTask.text = "倒计时（${time / 1000} 秒）"
    }

    override fun completed(task: CountDownTask) {
      btnCountDownTask.text = "倒计时完成"
    }

    override fun terminated(task: CountDownTask) {
      btnCountDownTask.text = "倒计时被中止"
    }
  })
}