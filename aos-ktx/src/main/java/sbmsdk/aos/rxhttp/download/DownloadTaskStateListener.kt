package sbmsdk.aos.rxhttp.download

/**
 * desc   :
 * date   : 2024/3/26
 *
 * @author zoulinheng
 */
interface DownloadTaskStateListener {

  fun onState(task: DownloadTask2, state: DownloadState)
}