package sbmsdk.aos.rxhttp.ktx

import android.util.Log
import androidx.annotation.Size
import com.blankj.utilcode.util.FileIOUtils
import sbmsdk.aos.doIfTrue
import sbmsdk.aos.rxhttp.config.PathConfig
import java.io.File

/**
 * desc   :
 * date   : 2023/12/7
 *
 * @author zoulinheng
 */

/**
 * 写入error日志
 */
internal fun writeLoge(@Size(min = 1) fileName: String, content: String, append: Boolean): String {
  loge(content)
  val path = PathConfig.EXCEPTION_LOG_DIR + File.separator + "$fileName.txt"
  writeLog(path, content, append)
  return path
}

/**
 * 写入waring日志
 */
internal fun writeLogw(@Size(min = 1) fileName: String, content: String, append: Boolean, showLog: Boolean = false): String {
  showLog.doIfTrue {
    logw(content)
  }
  val path = PathConfig.WARING_LOG_DIR + File.separator + "$fileName.txt"
  writeLog(path, content, append)
  return path
}

/**
 * 写入日志
 */
internal fun writeLog(filePath: String, content: String, append: Boolean, showLog: Boolean = false) {
  showLog.doIfTrue {
    logw(content)
  }
  FileIOUtils.writeFileFromString(filePath, content, append)
}

internal fun logd(msg: String, tag: String? = null) {
  Log.d(tag ?: TAG, msg)
}

internal fun loge(msg: String, tag: String? = null) {
  Log.e(tag ?: TAG, msg)
}

internal fun logi(msg: String, tag: String? = null) {
  Log.i(tag ?: TAG, msg)
}

internal fun logv(msg: String, tag: String? = null) {
  Log.v(tag ?: TAG, msg)
}

internal fun logw(msg: String, tag: String? = null) {
  Log.w(tag ?: TAG, msg)
}

private const val TAG = "App日志"
