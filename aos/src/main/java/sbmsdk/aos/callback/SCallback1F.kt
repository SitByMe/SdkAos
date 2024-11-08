package sbmsdk.aos.callback

/**
 * desc   :
 * date   : 2024/7/29
 *
 * @author zoulinheng
 */
interface SCallback1F<C, FS : SFailedStatus> {
  fun callback(var1: C)

  fun failed(status: FS)
}