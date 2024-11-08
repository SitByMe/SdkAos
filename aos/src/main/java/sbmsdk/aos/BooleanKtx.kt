package sbmsdk.aos

/**
 * desc   :
 * date   : 2023/9/14
 *
 * @author zoulinheng
 */

/**
 * 当 [this] 为 true 时执行 [action]
 */
fun Boolean.doIfTrue(action: () -> Unit): Boolean {
  if (this) action()
  return this
}

/**
 * 当 [this] 为 false 时执行 [action]
 */
fun Boolean.doIfFalse(action: () -> Unit): Boolean {
  if (!this) action()
  return this
}

/**
 * 如果[this]为true，则返回[action]的返回值
 */
fun Boolean.ifTrue(action: () -> Boolean): Boolean {
  return ifElse(action.invoke(), this)
}

/**
 * 如果[this]为false，则返回[action]的返回值
 */
fun Boolean.ifFalse(action: () -> Boolean): Boolean {
  return ifElse(this, action.invoke())
}

/**
 * 当 [this] 为 true 时返回 [t]
 * 当 [this] 为 false 时返回 [f]
 */
fun <T> Boolean.ifElse(t: T, f: T): T {
  return if (this) {
    t
  } else {
    f
  }
}