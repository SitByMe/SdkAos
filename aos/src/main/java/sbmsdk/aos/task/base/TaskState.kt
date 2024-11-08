package sbmsdk.aos.task.base

/**
 * desc   : 任务状态
 * date   : 2023/6/16
 * @author zoulinheng
 */
enum class TaskState {
  NEW,        //新建
  READY,      //就绪
  RUNNING,    //进行中
  TERMINATED  //终止
}