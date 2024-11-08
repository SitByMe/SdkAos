package sbm.demo.sdk.aos.act.http.frg.bean

import java.util.*

/**
 * desc   :
 * date   : 2024/5/13
 * @author zoulinheng
 */
class UpV5UrlVo(
  private val zfId: String,
  private val adjunctType: String
) {

  fun getUrl(): String {
    val name = zfId.lowercase(Locale.getDefault())
    return "http://192.168.4.8:1280/Upload.ashx?name=$name|$name.jpeg&AdjunctType=$adjunctType"
  }
}