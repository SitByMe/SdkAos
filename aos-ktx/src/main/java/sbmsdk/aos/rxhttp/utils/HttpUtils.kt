package sbmsdk.aos.rxhttp.utils

import okhttp3.MediaType
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * http工具类
 */
object HttpUtils {

  @JvmField
  val UTF8: Charset = StandardCharsets.UTF_8

  /**
   * 判断请求响应内容是否是人能读懂的内容
   */
  @JvmStatic
  fun isPlaintext(mediaType: MediaType?): Boolean {
    if (mediaType == null) {
      return false
    }
    if (mediaType.type == "text") {
      return true
    }
    var subtype = mediaType.subtype
    subtype = subtype.lowercase(Locale.CHINA)
    return subtype.contains("x-www-form-urlencoded") ||
           subtype.contains("json") ||
           subtype.contains("xml") ||
           subtype.contains("html")
  }

  /**
   * 解析前：https://xxx.xxx.xxx/app/chairdressing/skinAnalyzePower/skinTestResult?appId=10101
   * 解析后：https://xxx.xxx.xxx/app/chairdressing/skinAnalyzePower/skinTestResult
   */
  @JvmStatic
  fun parseUrl(url: String): String {
    if ("" != url && url.contains("?")) { // 如果URL不是空字符串
      return url.substring(0, url.indexOf('?'))
    }
    return url
  }

  /**
   * 将参数拼接到url中
   *
   * @param url    请求的url
   * @param params 参数
   * @return
   */
  @JvmStatic
  fun createUrlFromParams(url: String, params: Map<String?, Any>): String {
    try {
      val sb = StringBuilder()
      sb.append(url)
      if (url.indexOf('&') > 0 || url.indexOf('?') > 0) sb.append("&") else sb.append("?")
      for ((key, value) in params) {
        val urlValues = value.toString()
        //对参数进行 utf-8 编码,防止头信息传中文
        val urlValue = URLEncoder.encode(urlValues, UTF8.name())
        sb.append(key).append("=").append(urlValue).append("&")
      }
      sb.deleteCharAt(sb.length - 1)
      return sb.toString()
    } catch (e: UnsupportedEncodingException) {
      HttpLog.e(e)
    }
    return url
  }

  internal fun <T> T.doIf(predicate: Boolean, block: T.() -> T): T {
    return if (predicate) {
      block(this)
    } else
      this
  }
}