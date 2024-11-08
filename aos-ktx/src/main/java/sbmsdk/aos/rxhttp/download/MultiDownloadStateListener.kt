package sbmsdk.aos.rxhttp.download

/**
 * desc   :
 * date   : 2024/5/8
 *
 * @author zoulinheng
 */
interface MultiDownloadStateListener {

  fun onState(state: MultiDownloadState)
}