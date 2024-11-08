package sbmsdk.aos.rxhttp.helper

import sbmsdk.aos.toCalendar
import java.util.*

/**
 * desc   : Java服务端辅助类
 * date   : 2024/3/8
 * @author zoulinheng
 */
object JavaServerHelper {

  //java服务器与设备时间的偏移量（java服务器时间 - 设备时间）
  private var scTimeOffset: Long = 0

  /**
   * 设置服务器时间
   */
  fun setServerTime(time: Long) {
    scTimeOffset = time - devCalendar.timeInMillis
  }

  fun getCalendar(): Calendar {
    return getTime().toCalendar()
  }

  /**
   * 获取服务器时间
   */
  fun getTime(): Long {
    return devCalendar.timeInMillis + scTimeOffset
  }

  fun getOffset(): Long = scTimeOffset

  private val devCalendar: Calendar get() = Calendar.getInstance()
}