package sbmsdk.aos.rxhttp.utils

import android.util.Log

/**
 * 框架日志记录
 */
object HttpLog {

  private const val DEFAULT_LOG_TAG = "SHttp--->"
  private const val MAX_LOG_PRIORITY = 10         //最大日志优先级【日志优先级为最大等级，所有日志都不打印】
  private const val MIN_LOG_PRIORITY = 0          // 最小日志优先级【日志优先级为最小等级，所有日志都打印】
  private var sILogger: ILogger = LogcatLogger  // 默认的日志记录为Logcat
  private var sTag = DEFAULT_LOG_TAG
  private var sIsDebug = false                    //是否是调试模式
  private var sLogPriority = MAX_LOG_PRIORITY     //日志打印优先级

  fun setLogger(logger: ILogger) {
    sILogger = logger
  }

  fun setTag(tag: String) {
    sTag = tag
  }

  private fun setDebug(isDebug: Boolean) {
    sIsDebug = isDebug
  }

  private fun setPriority(priority: Int) {
    sLogPriority = priority
  }

  fun debug(isDebug: Boolean) {
    if (isDebug) {
      debug(DEFAULT_LOG_TAG)
    } else {
      debug("")
    }
  }

  fun debug(tag: String) {
    if (tag.isNotEmpty()) {
      setDebug(true)
      setPriority(MIN_LOG_PRIORITY)
      setTag(tag)
    } else {
      setDebug(false)
      setPriority(MAX_LOG_PRIORITY)
      setTag("")
    }
  }

  fun v(msg: String?) {
    if (enableLog(Log.VERBOSE)) {
      sILogger.log(Log.VERBOSE, sTag, msg, null)
    }
  }

  /**
   * 打印调试信息
   *
   * @param msg
   */
  fun d(msg: String?) {
    if (enableLog(Log.DEBUG)) {
      sILogger.log(Log.DEBUG, sTag, msg, null)
    }
  }

  /**
   * 打印提示性的信息
   *
   * @param msg
   */
  @JvmStatic
  fun i(msg: String?) {
    if (enableLog(Log.INFO)) {
      sILogger.log(Log.INFO, sTag, msg, null)
    }
  }

  /**
   * 打印warning警告信息
   *
   * @param msg
   */
  fun w(msg: String?) {
    if (enableLog(Log.WARN)) {
      sILogger.log(Log.WARN, sTag, msg, null)
    }
  }

  /**
   * 打印出错信息
   *
   * @param msg
   */
  fun e(msg: String?) {
    if (enableLog(Log.ERROR)) {
      sILogger.log(Log.ERROR, sTag, msg, null)
    }
  }

  /**
   * 打印出错堆栈信息
   *
   * @param t
   */
  @JvmStatic
  fun e(t: Throwable?) {
    if (enableLog(Log.ERROR)) {
      sILogger.log(Log.ERROR, sTag, null, t)
    }
  }

  /**
   * 打印出错信息
   *
   * @param msg
   * @param t
   */
  fun e(msg: String?, t: Throwable?) {
    if (enableLog(Log.ERROR)) {
      sILogger.log(Log.ERROR, sTag, msg, t)
    }
  }

  /**
   * 打印严重的错误信息
   *
   * @param msg
   */
  fun wtf(msg: String?) {
    if (enableLog(Log.ASSERT)) {
      sILogger.log(Log.ASSERT, sTag, msg, null)
    }
  }

  /**
   * 能否打印
   *
   * @param logPriority
   *
   * @return
   */
  private fun enableLog(logPriority: Int): Boolean {
    return sIsDebug && logPriority >= sLogPriority
  }
}