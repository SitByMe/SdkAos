package sbmsdk.aos.rxhttp

import rxhttp.RxHttp
import rxhttp.RxHttpBodyParam
import rxhttp.RxHttpNoBodyParam

/**
 * desc   :
 * date   : 2022/6/1
 *
 * @author zoulinheng
 */
object SRequest {

  /*---------- Get ----------*/

  /**
   * 创建Get请求
   *
   * @param url       完整url地址
   * @param params    参数
   * @param headers   header
   */

  fun createGet(url: String, params: Map<String, Any?>? = null, headers: Map<String, String>? = null): RxHttpNoBodyParam {
    return RxHttp.get(url).apply {
      headers?.let { addAllHeader(it) }
      params?.let { addAllQuery(it) }
    }
  }

  suspend inline fun <reified R> requestGet(url: String, params: Map<String, Any?>? = null, headers: Map<String, String>? = null): HttpLoadResult<R> {
    return RxHttp.get(url).apply {
      headers?.let { addAllHeader(it) }
      params?.let { addAllQuery(it) }
    }.toClassAwait()
  }

  /*---------- Post ----------*/

  /**
   * 创建Post请求
   *
   * @param url       完整url地址
   * @param params    参数
   * @param headers   header
   */

  fun createPostBody(url: String, params: Map<String, Any?> = emptyMap(), headers: Map<String, String>? = null): RxHttpBodyParam {
    return RxHttp.postBody(url).apply {
      headers?.let { addAllHeader(it) }
      setBody(params)
    }
  }

  fun createPostBody(url: String, params: Any, headers: Map<String, String>? = null): RxHttpBodyParam {
    return RxHttp.postBody(url).apply {
      headers?.let { addAllHeader(it) }
      setBody(params)
    }
  }

  suspend inline fun <reified R> requestPostBody(url: String, params: Any? = null, headers: Map<String, String>? = null): HttpLoadResult<R> {
    return RxHttp.postBody(url).apply {
      headers?.let { addAllHeader(it) }
      params?.let { setBody(it) }
    }.toClassAwait()
  }

  /*---------- Put ----------*/

  fun createPutBody(url: String, params: Map<String, Any?> = emptyMap(), headers: Map<String, String>? = null): RxHttpBodyParam {
    return RxHttp.putBody(url).apply {
      headers?.let { addAllHeader(it) }
      setBody(params)
    }
  }

  fun createPutBody(url: String, params: Any? = null, headers: Map<String, String>? = null): RxHttpBodyParam {
    return RxHttp.putBody(url).apply {
      headers?.let { addAllHeader(it) }
      params?.let { setBody(it) }
    }
  }

  /*---------- delete ----------*/

  fun createDeleteBody(url: String, params: Map<String, Any?> = emptyMap(), headers: Map<String, String>? = null): RxHttpBodyParam {
    return RxHttp.deleteBody(url).apply {
      headers?.let { addAllHeader(it) }
      setBody(params)
    }
  }

  fun createDeleteBody(url: String, params: Any? = null, headers: Map<String, String>? = null): RxHttpBodyParam {
    return RxHttp.putBody(url).apply {
      headers?.let { addAllHeader(it) }
      params?.let { setBody(it) }
    }
  }
}
