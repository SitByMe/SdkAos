package sbmsdk.aos

import java.text.SimpleDateFormat
import java.util.*

/**
 * desc   :
 * date   : 2022/8/18
 *
 * @author zoulinheng
 */

/**
 * 获取 Date 当天开始时间
 */
fun Date.getDayStart(): Calendar = Calendar.getInstance().apply { time = this@getDayStart }.start(UNIT_DAY)

/**
 * 获取 Date 当天结束时间
 */
fun Date.getDayEnd(): Calendar = Calendar.getInstance().apply { time = this@getDayEnd }.end(UNIT_DAY)

/**
 * 获取 Date 当周开始时间
 */
fun Date.getWeekStart(): Calendar = Calendar.getInstance().apply { time = this@getWeekStart }.start(UNIT_WEEK)

/**
 * 获取 Date 当周结束时间
 */
fun Date.getWeekEnd(): Calendar = Calendar.getInstance().apply { time = this@getWeekEnd }.end(UNIT_WEEK)

/**
 * 获取 Date 当月开始时间
 */
fun Date.getMonthStart(): Calendar = Calendar.getInstance().apply { time = this@getMonthStart }.start(UNIT_MONTH)

/**
 * 获取 Date 当月结束时间
 */
fun Date.getMonthEnd(): Calendar = Calendar.getInstance().apply { time = this@getMonthEnd }.end(UNIT_MONTH)

/**
 * 获取 Date 当年开始时间
 */
fun Date.getYearStart(): Calendar = Calendar.getInstance().apply { time = this@getYearStart }.start(UNIT_YEAR)

/**
 * 获取 Date 当年结束时间
 */
fun Date.getYearEnd(): Calendar = Calendar.getInstance().apply { time = this@getYearEnd }.end(UNIT_YEAR)

/**
 * 获取 Date 偏移后的日期
 *
 * @param offset    偏移量
 * @param unit      单位，see [UNIT_YEAR]、[UNIT_MONTH]、[UNIT_WEEK]、[UNIT_DAY]、[UNIT_HOUR]、[UNIT_MINUTE]、[UNIT_SECOND]、[UNIT_MILLISECOND]
 */
fun Date.offset(offset: Int, unit: Int): Date {
  val mCalendar = Calendar.getInstance()
  mCalendar.time = this
  when (unit) {
    UNIT_YEAR        -> mCalendar.add(Calendar.YEAR, offset)
    UNIT_MONTH       -> mCalendar.add(Calendar.MONTH, offset)
    UNIT_WEEK        -> mCalendar.add(Calendar.WEEK_OF_YEAR, offset)
    UNIT_DAY         -> mCalendar.add(Calendar.DAY_OF_YEAR, offset)
    UNIT_HOUR        -> mCalendar.add(Calendar.HOUR_OF_DAY, offset)
    UNIT_MINUTE      -> mCalendar.add(Calendar.MINUTE, offset)
    UNIT_SECOND      -> mCalendar.add(Calendar.SECOND, offset)
    UNIT_MILLISECOND -> mCalendar.add(Calendar.MILLISECOND, offset)
  }
  return mCalendar.time
}

/**
 * 将 Date 转成格式化的时间文字
 *
 * @param pattern       格式，比如：yyyy/MM/dd HH:mm
 * @param defaultValue  默认值
 */
fun Date.toString(pattern: String, defaultValue: String = "", locale: Locale = Locale.ROOT): String {
  val format = try {
    SimpleDateFormat(pattern, locale)
  } catch (e: Exception) {
    return defaultValue
  }
  return format.format(this)
}