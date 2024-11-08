package sbmsdk.aos

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * desc   :
 * date   : 2022/9/23
 *
 * @author zoulinheng
 */

/**
 * 将 Double 保存[number]位小数的字符串
 */
fun Double.toString(number: Int): String {
  return DecimalFormat().apply {
    maximumFractionDigits = number
    groupingSize = 0
    roundingMode = RoundingMode.FLOOR
  }.format(this)
}