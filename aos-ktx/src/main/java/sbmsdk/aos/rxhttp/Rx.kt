package sbmsdk.aos.rxhttp

import android.util.Log
import rxhttp.toClass
import rxhttp.toList
import rxhttp.toStr
import rxhttp.tryAwait
import rxhttp.wrapper.CallFactory

/**
 * desc   :
 * date   : 2022/5/30
 *
 * @author zoulinheng
 */
suspend inline fun <reified T> CallFactory.toClassAwait(): HttpLoadResult<T> {
  var throwable: Throwable? = null
  val response = this.toClass<T>().tryAwait { throwable = it }
  return when {
    throwable != null -> {
      Log.e("拦截一下RxHttp异常", throwable?.message ?: "消息为空")
      if (throwable?.message == "Job was cancelled") {
        HttpLoadResult.Failed(throwable = RuntimeException(""))
      } else {
        HttpLoadResult.Failed(throwable = throwable!!)
      }
    }
    response == null  -> HttpLoadResult.Failed(throwable = NullPointerException("result data is null"))
    else              -> HttpLoadResult.Success(data = response)
  }
}

suspend inline fun CallFactory.toStrAwait(): HttpLoadResult<String> {
  var throwable: Throwable? = null
  val response = this.toStr().tryAwait { throwable = it }
  return when {
    throwable != null -> {
      Log.e("拦截一下RxHttp异常", throwable?.message ?: "消息为空")
      if (throwable?.message == "Job was cancelled") {
        HttpLoadResult.Failed(throwable = RuntimeException(""))
      } else {
        HttpLoadResult.Failed(throwable = throwable!!)
      }
    }
    response == null  -> HttpLoadResult.Failed(throwable = NullPointerException("result data is null"))
    else              -> HttpLoadResult.Success(data = response)
  }
}

suspend inline fun <reified T> CallFactory.toListAwait(): HttpLoadResult<MutableList<T>> {
  var throwable: Throwable? = null
  val response = this.toList<T>().tryAwait {
    throwable = it
  }
  return when {
    throwable != null -> HttpLoadResult.Failed(throwable = throwable!!)
    response == null  -> HttpLoadResult.Failed(throwable = NullPointerException("result data is null"))
    else              -> HttpLoadResult.Success(data = response)
  }
}