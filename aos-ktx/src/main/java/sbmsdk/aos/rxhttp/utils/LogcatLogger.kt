package sbmsdk.aos.rxhttp.utils

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

/**
 * 默认Logcat日志记录
 */
object LogcatLogger : ILogger {

  override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
    var s = message
    if (s != null && s.isEmpty()) {
      s = null
    }
    if (s == null) {
      if (t == null) {
        return
      }
      s = getStackTraceString(t)
    } else {
      if (t != null) {
        s += """
                ${getStackTraceString(t)}
                """.trimIndent()
      }
    }
    log(priority, tag, s)
  }

  private fun getStackTraceString(t: Throwable): String {
    val sw = StringWriter(256)
    val pw = PrintWriter(sw, false)
    t.printStackTrace(pw)
    pw.flush()
    return sw.toString()
  }

  private fun log(priority: Int, tag: String?, message: String?) {
    val subNum = message!!.length / MAX_LOG_LENGTH
    if (subNum > 0) {
      var index = 0
      for (i in 0 until subNum) {
        val lastIndex = index + MAX_LOG_LENGTH
        val sub = message.substring(index, lastIndex)
        logSub(priority, tag!!, sub)
        index = lastIndex
      }
      logSub(priority, tag!!, message.substring(index))
    } else {
      logSub(priority, tag!!, message)
    }
  }

  /**
   * 使用LogCat输出日志.
   *
   * @param priority 优先级
   * @param tag      标签
   * @param sub      信息
   */
  private fun logSub(priority: Int, tag: String, sub: String) {
    when (priority) {
      Log.VERBOSE -> Log.v(tag, sub)
      Log.DEBUG   -> Log.d(tag, sub)
      Log.INFO    -> Log.i(tag, sub)
      Log.WARN    -> Log.w(tag, sub)
      Log.ERROR   -> Log.e(tag, sub)
      Log.ASSERT  -> Log.wtf(tag, sub)
      else        -> Log.v(tag, sub)
    }
  }
}

private const val MAX_LOG_LENGTH = 4000
