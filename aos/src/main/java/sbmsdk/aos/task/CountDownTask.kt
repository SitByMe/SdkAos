package sbmsdk.aos.task

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import sbmsdk.aos.lifecycle.EmptyActivityLifecycleCallbacks
import sbmsdk.aos.task.base.BaseTask
import sbmsdk.aos.task.base.TaskState
import java.time.Duration
import java.time.ZonedDateTime
import java.util.*

/**
 * desc   : 倒计时任务
 * date   : 2023/2/23
 * @author zoulinheng
 *
 * @property activity           如果不为null，则将倒计时任务与该activity的生命周期进行绑定
 * @property countDownCallback  倒计时回调
 */
class CountDownTask(val activity: Activity?, private val countDownCallback: CountDownCallback) : BaseTask() {

  private val lifecycleCallback = object : EmptyActivityLifecycleCallbacks {
    override fun onActivityDestroyed(p0: Activity) {
      if (activity == null) return
      if (activity != p0) return
      super.onActivityDestroyed(p0)
      terminate()
      destroy()
    }
  }

  init {
    activity?.application?.registerActivityLifecycleCallbacks(lifecycleCallback)
    new()
  }

  private fun destroy() {
    activity?.application?.unregisterActivityLifecycleCallbacks(lifecycleCallback)
  }


  private var handler: Handler? = null

  /**
   * 结束时间
   */
  private var endTime: Long = -1

  /**
   * 传入结束时间并开启任务（如果任务正在执行，则覆盖原来的倒计时剩余时长）
   *
   * @param endTime 结束时间
   */
  @Synchronized
  fun start(endTime: Long) {
    run()
    resetEndTime(endTime)
  }

  /**
   * 停止任务（会清空剩余时长）
   */
  fun stop() {
    terminate()
  }

  /**
   * 暂停
   */
  fun pause(): Boolean {
    return if (!paused) {
      paused = true
      pausedTime = System.currentTimeMillis()
      true
    } else {
      false
    }
  }

  /**
   * （暂停后）重新启动
   */
  fun resume(): Boolean {
    return if (paused) {
      paused = false
      pausedTime = null
      true
    } else {
      false
    }
  }

  /**
   * 重置结束时间
   */
  private fun resetEndTime(endTime: Long) {
    this.endTime = endTime
    postTime()
  }

  interface CountDownCallback {

    /**
     * 剩余时长
     */
    fun remainTime(time: Long)

    /**
     * 完成
     */
    fun completed(task: CountDownTask)

    /**
     * 终止
     */
    fun terminated(task: CountDownTask)
  }

  //是否已暂停
  private var paused = false
    set(value) {
      if (field == value) return
      field = value
      remainTimeWhenPaused = if (paused) {
        this.endTime - System.currentTimeMillis()
      } else {
        remainTimeWhenPaused?.let {
          resetEndTime(System.currentTimeMillis() + it)
        }
        null
      }
    }

  //暂停时间
  private var pausedTime: Long? = null

  //暂停时候的剩余时长
  private var remainTimeWhenPaused: Long? = null
  private val task = object : Runnable {
    override fun run() {
      if (isRunning()) {
        postTime()
        handler?.postDelayed(this, getPostTime())
      }
    }
  }

  override fun onTaskStateChanged(newTaskState: TaskState) {
    when (newTaskState) {
      TaskState.NEW        -> Unit
      TaskState.READY      -> Unit
      TaskState.RUNNING    -> {
        if (handler == null) {
          handler = Handler(Looper.getMainLooper())
        }
        handler?.run {
          removeCallbacks(task)
          post(task)
        }
      }
      TaskState.TERMINATED -> {
        handler?.removeCallbacks(task)
        countDownCallback.terminated(this)
        resetEndTime(-1)
      }
    }
  }

  private fun getPostTime(): Long {
    val mTime = Calendar.getInstance()
    var millisUntilNextTick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val now = mTime.toInstant()
      val zone = mTime.timeZone.toZoneId()
      val nextTick: ZonedDateTime = now.atZone(zone).plusSeconds(1).withNano(0)
      Duration.between(now, nextTick.toInstant()).toMillis()
    } else {
      1000
    }
    if (millisUntilNextTick <= 0) {
      millisUntilNextTick = 1000
    }
    return millisUntilNextTick
  }

  /**
   * 发送一次时间
   */
  private fun postTime() {
    val now = System.currentTimeMillis()
    //剩余时长
    val remainTime = this.endTime - now
    println("============ remainTime:$remainTime")
    if (remainTime >= 0) {
      countDownCallback.remainTime(time = remainTime)
    } else {
      countDownCallback.completed(this)
      success()
    }
  }
}