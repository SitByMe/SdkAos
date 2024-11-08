package sbmsdk.aos.rxhttp.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import sbmsdk.aos.rxhttp.utils.HttpUtils
import java.io.IOException

/**
 * 基础拦截器
 */
abstract class BaseInterceptor : Interceptor {

  /**
   * 请求拦截
   * 返回 `null` -> 不进行拦截处理
   */
  open fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request? = null

  /**
   * 响应拦截
   * 返回 `null` -> 不进行拦截处理
   */
  protected abstract fun onAfterRequest(response: Response, chain: Interceptor.Chain, bodyString: String): Response

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = onBeforeRequest(chain.request(), chain) ?: chain.request()
    val response = chain.proceed(request)
    return if (response.body?.contentType().isText) {
      onAfterRequest(response, chain, getResponseBodyString(response))
    } else {
      response
    }
  }

  @Throws(IOException::class)
  private fun getResponseBodyString(response: Response): String {
    val responseBody = response.body
    val source = responseBody?.source() ?: return ""
    return try {
      source.request(Long.MAX_VALUE)
      source.buffer.clone().readString(responseBody.contentType()?.charset(HttpUtils.UTF8) ?: HttpUtils.UTF8)
    } catch (e: Exception) {
      if (e is IOException) throw e
      else ""
    }
  }

  private val MediaType?.isText: Boolean get() = this != null && (this.type == "text" || this.subtype == "json")
}