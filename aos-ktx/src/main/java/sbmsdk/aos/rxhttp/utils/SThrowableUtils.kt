package sbmsdk.aos.rxhttp.utils

import android.util.Log
import rxhttp.wrapper.exception.HttpStatusCodeException
import java.net.ConnectException

/**
 * desc   :
 * date   : 2024/5/31
 *
 * @author zoulinheng
 */
object SThrowableUtils {

  fun getMessage(throwable: Throwable): String {
    return message(throwable) {
      it.localizedMessage ?: ""
    }
  }

  fun getStackTraceString(throwable: Throwable): String {
    return message(throwable) {
      Log.getStackTraceString(it)
    }
  }

  private fun message(throwable: Throwable, msgCreator: (Throwable) -> String): String {
    return when (throwable) {
      is HttpStatusCodeException ->
        when (throwable.statusCode) {
          404  -> "文件不存在：${throwable.httpUrl}"
          else -> msgCreator(throwable)
        }
      is ConnectException        -> throwable.localizedMessage ?: ""
      else                       -> msgCreator(throwable)
    }
  }
}

fun Throwable.getEjyMessage(): String = SThrowableUtils.getMessage(this)

fun Throwable.getEjyStackTraceString(): String = SThrowableUtils.getStackTraceString(this)