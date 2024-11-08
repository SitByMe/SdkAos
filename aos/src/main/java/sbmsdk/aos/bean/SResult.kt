package sbmsdk.aos.bean

/**
 * desc   :
 * date   : 2024/3/2
 *
 * @author zoulinheng
 *
 * 只是为了处理原生的[Result]对象不发使用 when(this) 的自动补全特性的问题。
 */
sealed class SResult<T : Any> {
  class Success<T : Any> internal constructor(val value: T?) : SResult<T>() {

    override fun toString(): String {
      return "Success(value=$value)"
    }
  }

  class Failed<T : Any> internal constructor(val throwable: Throwable) : SResult<T>() {
    internal constructor(text: String) : this(RuntimeException(text))

    override fun toString(): String {
      return "Failed(throwable=$throwable)"
    }
  }

  companion object {
    fun <T : Any> success(value: T?): Success<T> = Success(value)
    fun <T : Any> failed(throwable: Throwable): Failed<T> = Failed(throwable)
    fun <T : Any> failed(text: String): Failed<T> = Failed(text)
  }
}

fun <T : Any> Result<T>.toWResult(): SResult<T> {
  return if (this.isSuccess) {
    SResult.success(this.getOrNull())
  } else {
    this.exceptionOrNull()?.let {
      SResult.failed(it)
    } ?: SResult.failed("出现异常")
  }
}