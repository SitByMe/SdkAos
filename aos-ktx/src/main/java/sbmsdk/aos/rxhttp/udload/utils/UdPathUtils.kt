package sbmsdk.aos.rxhttp.udload.utils

import androidx.annotation.Size
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.PathUtils
import java.io.File

/**
 * desc   :
 * date   : 2024/3/1
 *
 * @author zoulinheng
 */
object UdPathUtils {

  //获取下载的保存地址
  fun getDownloadDirPath(@Size(min = 1) adjunctType: String): String {
    return "${PathUtils.getExternalDownloadsPath()}${File.separator}${AppUtils.getAppPackageName()}${File.separator}$adjunctType"
  }

  //获取下载的缓存地址
  fun getDownloadTempDirPath(@Size(min = 1) adjunctType: String): String {
    return "${PathUtils.getExternalAppCachePath()}${File.separator}Download${File.separator}temp${File.separator}$adjunctType"
  }
}