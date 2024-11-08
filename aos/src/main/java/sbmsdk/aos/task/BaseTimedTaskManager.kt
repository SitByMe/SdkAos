package sbmsdk.aos.task

import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ThreadUtils
import sbmsdk.aos.toJsonOrNull

/**
 * desc   : 定时任务管理器
 * date   : 2023/4/17
 * @author zoulinheng
 *
 * @property T  定时任务类型
 *
 * @param managerName 管理者名字。可根据不同的管理者名字分别对自身的定时任务进行管理。
 */
open class BaseTimedTaskManager<T : ITimedTask>(private val managerName: String) {
  private val spUtils: SPUtils
    get() = SPUtils.getInstance(managerName.ifBlank { "sp_timed_task_" })

  private val keyTaskMap = mutableMapOf<String, CountDownTask>()
  private val taskCallbacks = mutableMapOf<String, List<T.() -> Unit>>()

  /**
   * 添加任务
   *
   * @param taskData    任务数据
   * @param taskAction  任务行为
   */
  fun addTask(taskData: T, taskAction: T.() -> Unit) {
    val key = taskData.getPrimaryKey()
    spUtils.put(key, taskData.toJsonOrNull())
    registerTask(key = key, taskAction = taskAction)
    val cdTask = getCountDownTask(taskData)
    cdTask.start(taskData.getActionTime())
  }

  private fun registerTask(key: String, taskAction: T.() -> Unit) {
    (taskCallbacks[key]?.toMutableList() ?: mutableListOf()).let {
      it.add(taskAction)
      taskCallbacks.put(key, it)
    }
  }

  private fun getCountDownTask(taskData: T): CountDownTask {
    val key = taskData.getPrimaryKey()
    var cTask = keyTaskMap[key]
    if (cTask == null) {
      val ld = CountDownTask(null, object : CountDownTask.CountDownCallback {
        override fun remainTime(time: Long) {
        }

        override fun completed(task: CountDownTask) {
          ThreadUtils.runOnUiThread {
            taskCallbacks[key]?.onEach {
              it.invoke(taskData)
              spUtils.remove(key)
              keyTaskMap.remove(key)
            }
          }
        }

        override fun terminated(task: CountDownTask) {
        }
      })
      keyTaskMap[key] = ld
      cTask = ld
    }
    return cTask
  }
}

interface ITimedTask {
  /**
   * 获取唯一key
   */
  fun getPrimaryKey(): String

  /**
   * 获取执行任务的时间
   */
  fun getActionTime(): Long
}