package sbmsdk.aos.callback

/**
 * desc   :
 * date   : 2024/10/28
 *
 * @author zoulinheng
 */
interface SFailedStatus {
  val message: String
}

class SimpleFailedStatus(text: String) : SFailedStatus {
  override val message: String = text
}