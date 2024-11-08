package sbmsdk.aos.rxhttp.download

/**
 * desc   :
 * date   : 2024/3/26
 *
 * @author zoulinheng
 */
interface DownloadStateListener {

  fun onState(state: DownloadState)
}