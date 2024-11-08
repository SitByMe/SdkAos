package sbmsdk.aos.rxhttp.utils

/**
 * 简易的日志记录接口
 */
fun interface ILogger {
  /**
   * 打印信息
   *
   * @param priority 优先级
   * @param tag      标签
   * @param message  信息
   * @param t        出错信息
   */
  fun log(priority: Int, tag: String?, message: String?, t: Throwable?)
}