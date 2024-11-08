package sbmsdk.aos.rxhttp.config

import com.blankj.utilcode.util.PathUtils
import java.io.File

/**
 * desc   : 路径配置
 * date   : 2023/9/19
 *
 * @author zoulinheng
 */
internal object PathConfig {
  //缓存目录
  private val appCacheDir = PathUtils.getExternalAppDataPath() + File.separator + "ww_http_cache" + File.separator

  //日志目录
  private val logDir = PathUtils.getExternalDownloadsPath() + File.separator

  //异常日志目录
  val EXCEPTION_LOG_DIR = logDir + "error_log"

  //waring日志目录
  val WARING_LOG_DIR = logDir + "error_log"

  //获取业务数据的缓存目录
  private fun createYwCacheDir(ywName: String): String {
    return "$appCacheDir${File.separator}$ywName"
  }
}