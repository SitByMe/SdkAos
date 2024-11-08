package sbmsdk.aos.task.base

import android.util.Log

/**
 * desc   :
 * date   : 2023/6/16
 * @author zoulinheng
 */
abstract class BaseTask {

  //任务状态
  private var taskState: TaskState? = null
    set(value) {
      if (field == value) return
      field = value
      field?.let {
        onTaskStateChanged(it)
      }
    }

  //任务终止状态
  private var taskTerminatedType: TaskTerminatedType? = null

  /**
   * 任务状态变更回调
   */
  abstract fun onTaskStateChanged(newTaskState: TaskState)

  /*---------- open api ----------*/

  /**
   * 任务是否已经准备好
   */
  fun isReady(): Boolean {
    return taskState == TaskState.READY
  }

  /**
   * 任务是否正在执行
   */
  fun isRunning(): Boolean {
    return taskState == TaskState.RUNNING
  }

  /**
   * 任务是否已经终止
   */
  fun isTerminated(): Boolean {
    return taskState == TaskState.TERMINATED
  }

  /**
   * 任务是否成功
   */
  fun isSuccess(): Boolean {
    return taskState == TaskState.TERMINATED && taskTerminatedType == TaskTerminatedType.SUCCESS
  }

  /**
   * 任务是否失败
   */
  fun isFailed(): Boolean {
    return taskState == TaskState.TERMINATED && taskTerminatedType == TaskTerminatedType.FAILED
  }

  /**
   * 任务是否因为其他原因终止了
   */
  fun isTerminatedForOther(): Boolean {
    return taskState == TaskState.TERMINATED && taskTerminatedType == TaskTerminatedType.OTHER
  }

  /*---------- protected api ----------*/

  /**
   * 任务创建完成
   */
  protected fun new() {
    if (taskState == null) {
      taskTerminatedType = null
      taskState = TaskState.NEW
    } else {
      Log.e("BaseTask.new", "任务已经创建完成，不能重复创建")
    }
  }

  /**
   * 任务准备完成
   */
  protected fun ready() {
    taskTerminatedType = null
    taskState = TaskState.READY
  }

  /**
   * 开始执行任务
   */
  protected fun run() {
    taskTerminatedType = null
    taskState = TaskState.RUNNING
  }

  /**
   * 任务成功
   */
  protected fun success() {
    taskTerminatedType = TaskTerminatedType.SUCCESS
    taskState = TaskState.TERMINATED
  }

  /**
   * 任务失败
   */
  protected fun failed() {
    taskTerminatedType = TaskTerminatedType.FAILED
    taskState = TaskState.TERMINATED
  }

  /**
   * 人为终止
   */
  protected fun terminate() {
    taskTerminatedType = TaskTerminatedType.OTHER
    taskState = TaskState.TERMINATED
  }
}