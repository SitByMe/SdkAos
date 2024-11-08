package sbmsdk.aos.rxhttp.download

import android.text.TextUtils
import android.util.Log
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL

/**
 * desc   :
 * date   : 2023/7/10
 *
 * @author zoulinheng
 */
object FileURLChecker {
  /**
   * 判断一个 fileUrl 的文件是否存在
   *
   * @param checkAvailable  是否校验下载地址有效性
   */
  fun isURLExists(fileUrl: String?, checkAvailable: Boolean = true, showNotice: Boolean = true): Result<String> {
    if (!checkAvailable) {
      return Result.success("可以正常下载")
    }
    try {
      if (showNotice) {
        //        LoadingDialog.show(topActivity, "正在检查文件...")
      }
      val url = URL(fileUrl)
      val connection = url.openConnection() as HttpURLConnection
      //      connection.instanceFollowRedirects = false
      connection.requestMethod = "HEAD"
      val connUrl = connection.url.toString()
      //      val headJson = GsonUtils.toJson(connection.headerFields).formatJson()
      //      LogUtils.e(headJson)
      val type = connection.getHeaderField("Content-Type")
      val length = connection.getHeaderField("Content-Length")
      if (type != null && type.contains("text/html") && TextUtils.equals(length, "107")) {
        Log.w("FileURLChecker", "不存在：$connUrl")
        return Result.failure(FileNotFoundException("文件不存在：$fileUrl"))
      }
      val responseCode = connection.responseCode
      return if (responseCode == HttpURLConnection.HTTP_OK
      //      || responseCode == HttpURLConnection.HTTP_MOVED_PERM
      //      || responseCode == HttpURLConnection.HTTP_MOVED_TEMP
      ) {
        Log.w("FileURLChecker", connUrl)
        Result.success("可以正常下载")
      } else {
        val exceptionText = connection.responseMessage
        Log.w("FileURLChecker", "异常：$exceptionText - $connUrl")
        Result.failure(RuntimeException(exceptionText))
      }
    } catch (e: Exception) {
      Log.w("FileURLChecker", "异常：$e")
      e.printStackTrace()
      return Result.failure(e)
    } finally {
      if (showNotice) {
        //        LoadingDialog.dismiss()
      }
    }
  }
}