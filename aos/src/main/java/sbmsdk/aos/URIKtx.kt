package sbmsdk.aos

import java.net.URI

/**
 * desc   :
 * date   : 2024/5/17
 *
 * @author zoulinheng
 */

/**
 * 获取服务器域名地址
 */
fun URI.getUrlIP(): String {
  val effectiveURI: URI = try {
    URI(this.scheme, this.userInfo, this.host, this.port, null, null, null)
  } catch (e: Exception) {
    return ""
  }
  return effectiveURI.toString()
}