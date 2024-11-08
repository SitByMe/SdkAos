package sbmsdk.aos.rxhttp

/**
 * desc   :
 * date   : 2022/6/7
 *
 * @author zoulinheng
 */
abstract class BaseHttpImpl {

  abstract fun getBaseUrl(): String

  fun createUrl(url: String, baseUrl: String? = null): String {
    val mUrl = if (url.startsWith("/")) {
      url.replaceFirst("/", "")
    } else {
      url
    }
    val mBaseUrl = if (baseUrl.isNullOrBlank()) {
      getBaseUrl()
    } else {
      baseUrl
    }
    val sUrl = if (mBaseUrl.endsWith("/")) {
      mBaseUrl
    } else {
      "$mBaseUrl/"
    }
    return "${sUrl}$mUrl"
  }

  fun createUrl(url: String, params: Map<String, Any>, baseUrl: String? = null): String {
    val bUrl = createUrl(url = url, baseUrl = baseUrl)
    val sb = StringBuilder()
    params.forEach { (s, any) ->
      if (sb.isNotEmpty()) {
        sb.append("&")
      }
      sb.append(s).append("=").append(any)
    }
    return "$bUrl?$sb"
  }
}
