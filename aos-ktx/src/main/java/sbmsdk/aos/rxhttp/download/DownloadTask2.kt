package sbmsdk.aos.rxhttp.download

import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.Response
import rxhttp.RxHttp
import rxhttp.awaitResult
import rxhttp.toDownload
import rxhttp.wrapper.OkHttpCompat
import rxhttp.wrapper.callback.OutputStreamFactory
import rxhttp.wrapper.entity.ExpandOutputStream
import sbmsdk.aos.bean.SResult
import sbmsdk.aos.bean.toWResult
import sbmsdk.aos.doIfNotNullOrBlank
import sbmsdk.aos.rxhttp.utils.getEjyStackTraceString
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLDecoder
import java.util.*

/**
 * desc   : 下载任务
 * date   : 2024/3/26
 *
 * @author zoulinheng
 *
 * @param taskId      任务id
 * @param url         下载地址
 * @param saveName    下载到本地后保存的文件名
 * @param subDirName  下载子目录路径。eg. /abc/xxx
 * @param rootDir     下载根目录路径。eg. /abc/xxx
 * @param scope
 */
class DownloadTask2(
  val taskId: String = UUID.randomUUID().toString(),
  private val url: String,
  private val saveName: String? = null,
  private val subDirName: String? = null,
  rootDir: String = PathUtils.getExternalAppDownloadPath(),
  scope: CoroutineScope? = null,
) {

  private val scope: CoroutineScope = scope ?: MainScope()

  private var saveDir = buildString {
    if (rootDir.endsWith(File.separator)) {
      append(rootDir.substringBeforeLast(File.separator))
    } else {
      append(rootDir)
    }
    subDirName.doIfNotNullOrBlank {
      if (!it.startsWith(File.separator)) {
        append(File.separator)
      }
      append(it)
    }
  }

  private var tempSaveDir = "$saveDir${File.separator}temp"

  private val downloadStateHolder = DownloadStateHolder()
  private val downloadProgressHolder = DownloadProgressHolder()

  /**
   * 设置下载进度监听
   */
  fun progress(launchIn: CoroutineScope? = null, listener: (Progress) -> Unit): DownloadTask2 {
    downloadProgressHolder.addProgressListener(launchIn ?: scope, object : ProgressListener {
      override fun onProgress(p: Progress) {
        listener.invoke(p)
      }
    })
    return this
  }

  /**
   * 设置下载状态监听
   */
  fun state(launchIn: CoroutineScope? = null, listener: (DownloadState) -> Unit): DownloadTask2 {
    downloadStateHolder.addStateListener(launchIn ?: scope, object : DownloadStateListener {
      override fun onState(state: DownloadState) {
        listener.invoke(state)
      }
    })
    return this
  }

  /**
   * 开始下载
   */
  fun start() {
    downloadStateHolder.updateState(DownloadState.Waiting())
    scope.launch {
      val r = RxHttp.get(url).toDownload(MyFileOutputStreamFactory(tempSaveDir, saveName)) {
        downloadStateHolder.updateState(DownloadState.Downloading())
        downloadProgressHolder.updateProgress(Progress(it.progress, it.currentSize, it.totalSize))
      }.awaitResult().toWResult()
      downloadStateHolder.updateState(
        when (r) {
          is SResult.Failed  -> DownloadState.Failed(r.throwable.getEjyStackTraceString())
          is SResult.Success -> {
            val fileName = r.value?.substringAfterLast("${File.separator}temp${File.separator}")
            val saveFile = "$saveDir${File.separator}$fileName"
            if (FileUtils.move(r.value, saveFile)) {
              DownloadState.Succeed(saveFile)
            } else {
              DownloadState.Failed("移动文件失败")
            }
          }
        }
      )
    }
  }

  private class MyFileOutputStreamFactory(
    private val localDir: String, private val saveName: String?,
  ) : OutputStreamFactory<String>() {

    override fun offsetSize(): Long = File(localDir).length()

    override fun getOutputStream(response: Response): ExpandOutputStream<String> {
      val fileName = saveName ?: response.findFilename() ?: "unknown.ww"
      return File("${localDir}${File.separator}$fileName").run {
        if (!FileUtils.createOrExistsFile(this)) {
          throw IOException("File $path create fail")
        }
        toOutputStream(response.append)
      }
    }

    private val Response.append get() = OkHttpCompat.header(this, "Content-Range") != null

    private fun File.toOutputStream(append: Boolean = false) = ExpandOutputStream(absolutePath, FileOutputStream(this, append))

    private fun Response.findFilename(): String? {
      OkHttpCompat.header(this, "Content-Disposition")?.let { disposition ->
        disposition.split(";").forEach {
          val keyValuePair = it.split("=")
          if (keyValuePair.size > 1) {
            return when (keyValuePair[0].trim()) {
              "filename"  -> {
                keyValuePair[1].run {
                  //matches "test.apk" or 'test.apk'
                  if (matches(Regex("^[\"'][\\s\\S]*[\"']\$"))) {
                    substring(1, length - 1)
                  } else {
                    this
                  }
                }
              }
              "filename*" -> {
                keyValuePair[1].run {
                  val firstIndex = indexOf("'")
                  val lastIndex = lastIndexOf("'")
                  if (firstIndex == -1 || lastIndex == -1 || firstIndex >= lastIndex) return null
                  URLDecoder.decode(substring(lastIndex + 1), substring(0, firstIndex))
                }
              }
              else        -> null
            }
          }
        }
      } ?: OkHttpCompat.header(this, "Result-Extension")?.let { ext ->
        return "unknown.$ext"
      }
      return null
    }
  }
}