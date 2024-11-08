package sbmsdk.aos.rxhttp

import sbmsdk.aos.doIfFalse
import sbmsdk.aos.doIfTrue
import sbmsdk.aos.rxhttp.response.IResponseResult
import sbmsdk.aos.showAppToastShort

/**
 * desc   :
 * date   : 2022/5/31
 *
 * @author zoulinheng
 */
sealed class HttpLoadResult<T> {

  class Success<T>(val data: T, message: String?) : HttpLoadResult<T>() {

    constructor(data: T) : this(data, null)

    override val message: String? = message?.let {
      when (data) {
        is IResponseResult<*> -> data.getMessage()
        else                  -> null
      }
    }
  }

  class Failed<T>(val throwable: Throwable) : HttpLoadResult<T>() {
    override val message: String? = throwable.message

    constructor(text: String) : this(throwable = RuntimeException(text))
  }

  abstract val message: String?
}

fun <T> HttpLoadResult<T>.toSuccess(showFailedToast: Boolean = false, failedAction: ((Throwable) -> Unit)? = null): T? {
  return when (this) {
    is HttpLoadResult.Failed  -> {
      showFailedToast.doIfTrue {
        showAppToastShort(this.throwable.message)
      }
      failedAction?.invoke(this.throwable)
      null
    }
    is HttpLoadResult.Success -> {
      val data = this.data
      when (data) {
        is IResponseResult<*> -> {
          data.getState().doIfFalse {
            showAppToastShort(data.getMessage())
          }
        }
        else                  -> Unit
      }
      data
    }
  }
}

fun <T, R> HttpLoadResult.Failed<T>.mapError(): HttpLoadResult.Failed<R> {
  return HttpLoadResult.Failed(throwable = this.throwable)
}

suspend fun <T, R> HttpLoadResult<T>.map(map: suspend (T) -> R): HttpLoadResult<R> {
  return when (this) {
    is HttpLoadResult.Failed  -> {
      HttpLoadResult.Failed(this.throwable)
    }
    is HttpLoadResult.Success -> {
      val t = this.data
      val r = map.invoke(t)
      HttpLoadResult.Success(r)
    }
  }
}

fun <T> HttpLoadResult<T>.isSuccess(): Boolean {
  return when (this) {
    is HttpLoadResult.Failed  -> false
    is HttpLoadResult.Success -> {
      when (val data = this.data) {
        is IResponseResult<*> -> data.getState()
        else                  -> true
      }
    }
  }
}

fun <T> HttpLoadResult<T>.isError(): Boolean {
  return when (this) {
    is HttpLoadResult.Failed  -> true
    is HttpLoadResult.Success -> {
      when (val data = this.data) {
        is IResponseResult<*> -> !data.getState()
        else                  -> false
      }
    }
  }
}