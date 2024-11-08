package sbmsdk.aos.rxhttp.response

/**
 * desc   : 请求返回的数据
 * date   : 2024/1/30
 *
 * @author zoulinheng
 */
interface IResponseResult<T> {

  fun getData(): T?

  fun getMessage(): String

  fun getState(): Boolean

  fun getTotalCount(): Int? = null
}

