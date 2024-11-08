package sbmsdk.aos.rxhttp.download

/**
 * desc   :
 * date   : 2024/5/8
 *
 * @author zoulinheng
 */
interface TaskProgressListener {

  fun onProgress(task: DownloadTask2, p: Progress)
}