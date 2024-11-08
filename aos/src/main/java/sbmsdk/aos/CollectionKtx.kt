package sbmsdk.aos

/**
 * desc   :
 * date   : 2023/9/15
 *
 * @author zoulinheng
 */

/**
 * 相较于 get 方法，校验了各种异常情况
 */
fun <E> Collection<E>.getOrNull(index: Int?): E? {
  if (index == null) return null
  if (index <= -1) return null
  if (index >= this.size) {
    return null
  }
  return this.elementAt(index)
}