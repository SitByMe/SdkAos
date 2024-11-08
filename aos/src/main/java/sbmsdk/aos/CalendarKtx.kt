package sbmsdk.aos

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

/**
 * desc  :
 * date  : 2022/8/17
 *
 * @author zoulinheng
 */

/*---------- 单位（注意：单位从大到小，常量值也必须要从大到小） ----------*/
const val UNIT_YEAR = 8         //年
const val UNIT_MONTH = 7        //月
const val UNIT_WEEK = 6         //周
const val UNIT_DAY = 5          //日
const val UNIT_HOUR = 4         //时
const val UNIT_MINUTE = 3       //分
const val UNIT_SECOND = 2       //秒
const val UNIT_MILLISECOND = 1  //毫秒

/**
 * 获取 Calendar 当天开始时间
 */
fun Calendar.getDayStart(): Calendar = this.start(UNIT_DAY)

/**
 * 获取 Calendar 当天结束时间
 */
fun Calendar.getDayEnd(): Calendar = this.end(UNIT_DAY)

/**
 * 获取 Calendar 当周开始时间
 */
fun Calendar.getWeekStart(): Calendar = this.start(UNIT_WEEK)

/**
 * 获取 Calendar 当周结束时间
 */
fun Calendar.getWeekEnd(): Calendar = this.end(UNIT_WEEK)

/**
 * 获取 Calendar 当月开始时间
 */
fun Calendar.getMonthStart(): Calendar = this.start(UNIT_MONTH)

/**
 * 获取 Calendar 当月结束时间
 */
fun Calendar.getMonthEnd(): Calendar = this.end(UNIT_MONTH)

/**
 * 获取 Calendar 当年开始时间
 */
fun Calendar.getYearStart(): Calendar = this.start(UNIT_YEAR)

/**
 * 获取 Calendar 当年结束时间
 */
fun Calendar.getYearEnd(): Calendar = this.end(UNIT_YEAR)

/*---------- offset fun ----------*/

/**
 * 获取 Calendar 偏移后的日期
 *
 * @param offset    偏移量
 * @param unit      单位，see [UNIT_YEAR]、[UNIT_MONTH]、[UNIT_WEEK]、[UNIT_DAY]、[UNIT_HOUR]、[UNIT_MINUTE]、[UNIT_SECOND]、[UNIT_MILLISECOND]
 */
fun Calendar.offset(offset: Int, unit: Int): Calendar {
  when (unit) {
    UNIT_YEAR        -> this.add(Calendar.YEAR, offset)
    UNIT_MONTH       -> this.add(Calendar.MONTH, offset)
    UNIT_WEEK        -> this.add(Calendar.WEEK_OF_YEAR, offset)
    UNIT_DAY         -> this.add(Calendar.DAY_OF_YEAR, offset)
    UNIT_HOUR        -> this.add(Calendar.HOUR_OF_DAY, offset)
    UNIT_MINUTE      -> this.add(Calendar.MINUTE, offset)
    UNIT_SECOND      -> this.add(Calendar.SECOND, offset)
    UNIT_MILLISECOND -> this.add(Calendar.MILLISECOND, offset)
  }
  return this
}

/*---------- to string fun ----------*/

/**
 * 将 Calendar 转成格式化的时间文字
 *
 * @param pattern       格式，比如：yyyy/MM/dd HH:mm
 * @param defaultValue  默认值
 */
fun Calendar.toString(pattern: String, defaultValue: String = "", locale: Locale = Locale.CHINA): String {
  val format = try {
    SimpleDateFormat(pattern, locale)
  } catch (e: Exception) {
    return defaultValue
  }
  return format.format(this.time)
}

/*---------- other type field convert to Calendar ----------*/

fun Date.toCalendar(): Calendar {
  return Calendar.getInstance().apply { time = this@toCalendar }
}

fun Long.toCalendar(): Calendar {
  return Calendar.getInstance().apply { timeInMillis = max(0, this@toCalendar) }
}

fun String.toCalendar(pattern: String, locale: Locale = Locale.CHINA): Calendar {
  val date = try {
    SimpleDateFormat(pattern, locale).parse(this)
  } catch (e: Exception) {
    throw e
  }
  return Calendar.getInstance().apply { time = date }
}

fun String.toCalendarOrNull(pattern: String, locale: Locale = Locale.CHINA): Calendar? {
  val date = try {
    SimpleDateFormat(pattern, locale).parse(this)
  } catch (e: Exception) {
    return null
  }
  return Calendar.getInstance().apply { time = date }
}

/**
 * 获取开始的时间
 *
 * @param unit 单位：see [UNIT_YEAR] [UNIT_MONTH] [UNIT_WEEK] [UNIT_DAY] [UNIT_HOUR] [UNIT_MINUTE] [UNIT_SECOND]
 */
fun Calendar.start(unit: Int): Calendar {
  val cld = Calendar.getInstance().apply {
    this.timeInMillis = this@start.timeInMillis
  }
  if (unit >= UNIT_YEAR) {
    cld.set(Calendar.MONTH, 0)
  }
  if (unit >= UNIT_MONTH) {
    cld.set(Calendar.DAY_OF_MONTH, 1)
  }
  if (unit == UNIT_WEEK) {
    cld.set(Calendar.DAY_OF_WEEK, 1)
  }
  if (unit >= UNIT_DAY) {
    cld.set(Calendar.HOUR_OF_DAY, 0)
  }
  if (unit >= UNIT_HOUR) {
    cld.set(Calendar.MINUTE, 0)
  }
  if (unit >= UNIT_MINUTE) {
    cld.set(Calendar.SECOND, 0)
  }
  if (unit >= UNIT_SECOND) {
    cld.set(Calendar.MILLISECOND, 0)
  }
  return cld
}

/**
 * 获取结束的时间
 *
 * @param unit 单位：see [UNIT_YEAR] [UNIT_MONTH] [UNIT_WEEK] [UNIT_DAY] [UNIT_HOUR] [UNIT_MINUTE] [UNIT_SECOND]
 */
fun Calendar.end(unit: Int): Calendar {
  val cld = Calendar.getInstance().apply {
    this.timeInMillis = this@end.timeInMillis
  }
  if (unit == UNIT_YEAR) {
    cld.set(Calendar.MONTH, 0)
    cld.set(Calendar.DAY_OF_MONTH, 1)
    cld.add(Calendar.YEAR, 1)
    cld.add(Calendar.DAY_OF_YEAR, -1)
  }
  if (unit == UNIT_MONTH) {
    cld.set(Calendar.DAY_OF_MONTH, 1)
    cld.add(Calendar.MONTH, 1)
    cld.add(Calendar.DAY_OF_MONTH, -1)
  }
  if (unit == UNIT_WEEK) {
    cld.set(Calendar.DAY_OF_WEEK, getActualMaximum(Calendar.DAY_OF_WEEK))
  }
  if (unit >= UNIT_DAY) {
    cld.set(Calendar.HOUR_OF_DAY, 23)
  }
  if (unit >= UNIT_HOUR) {
    cld.set(Calendar.MINUTE, 59)
  }
  if (unit >= UNIT_MINUTE) {
    cld.set(Calendar.SECOND, 59)
  }
  if (unit >= UNIT_SECOND) {
    cld.set(Calendar.MILLISECOND, 999)
  }
  return cld
}