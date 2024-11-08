package sbmsdk.aos.rxhttp.download

import kotlinx.coroutines.CoroutineScope

/**
 * desc   :
 * date   : 2024/3/26
 *
 * @author zoulinheng
 */

fun CoroutineScope.download(
  url: String,
  saveName: String? = null,
  subDirName: String? = null,
): DownloadTask2 {
  return DownloadTask2(url = url, saveName = saveName, subDirName = subDirName, scope = this)
}