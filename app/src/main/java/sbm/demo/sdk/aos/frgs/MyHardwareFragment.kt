package sbm.demo.sdk.aos.frgs

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.MemoryInfo
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.PhoneUtils
import com.blankj.utilcode.util.RomUtils
import com.blankj.utilcode.util.ScreenUtils
import sbm.demo.sdk.aos.R
import sbmsdk.aos.formatJson
import sbmsdk.aos.toJson
import java.io.IOException
import java.io.RandomAccessFile

/**
 * desc   :
 * date   : 2024/6/11
 * @author zoulinheng
 */
class MyHardwareFragment : Fragment() {
  private lateinit var tvDevInfo: AppCompatTextView
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_my_dev, container, false).apply {
      tvDevInfo = findViewById(R.id.tv_dev_info)
    }
  }

  override fun onResume() {
    super.onResume()
    tvDevInfo.text = devInfo
  }

  private val devInfo: String
    get() = buildString {
      append("CPU：").append("${getCPUUsage()}\n")
      append("MemoryInfo：").append("${getMemoryUsage()}\n")
      append("Processes：").append("${getProcesses().toJson().formatJson()}\n")
      append("设备信息：").append("\n")
      append("系统：${RomUtils.getRomInfo().let { it.name + " - " + it.version }}\n")
      append("品牌：${DeviceUtils.getManufacturer()}\n")
      append("型号：${DeviceUtils.getModel()}\n")
      append("MAC地址：${DeviceUtils.getMacAddress()}\n")
      append("ABIS：\n")
      DeviceUtils.getABIs().onEach {
        append("    $it\n")
      }
      append("AndroidId：${DeviceUtils.getAndroidID()}\n")
      append("android版本：${DeviceUtils.getSDKVersionName()}(${DeviceUtils.getSDKVersionCode()})\n\n")
      append("Root状态：${DeviceUtils.isDeviceRooted()}\n")
      append("Abd状态：${DeviceUtils.isAdbEnabled()}\n\n")
      append("设备DeviceId：${PhoneUtils.getDeviceId()}\n")
      append("是手机？：${PhoneUtils.isPhone()}\n")
      append("是模拟器？：${DeviceUtils.isEmulator()}\n")
      append("是平板？：${DeviceUtils.isTablet()}\n\n")

      append("IMEI：${PhoneUtils.getIMEI()}\n")
      append("IMSI：${PhoneUtils.getIMSI()}\n")
      append("MEID：${PhoneUtils.getMEID()}\n")
      append("Serial：${PhoneUtils.getSerial()}\n")
      append("PhoneType：${PhoneUtils.getPhoneType()}\n")

      append("\n\n\n状态信息：\n")
      append("网络是否可用：${NetworkUtils.getWifiEnabled()}\n")
      append("网络状态：${NetworkUtils.getNetworkType()}\n")
      append("SSID：${NetworkUtils.getSSID()}\n")
      append("IP 地址：\n")
      append("    ${NetworkUtils.getIPAddress(true)}\n")
      append("    ${NetworkUtils.getIPAddress(false)}\n")

      append("\n\n\n应用信息：\n")
      AppUtils.getAppInfo()?.let {
        append("应用名称：${it.name}\n")
        append("应用包名：${it.packageName}\n")
        append("packagePath：${it.packagePath}\n")
        append("应用版本：${it.versionName}(${it.versionCode})\n")
      }
      append("屏幕分辨率：${ScreenUtils.getScreenWidth()}x${ScreenUtils.getScreenHeight()}\n")
      append("应用分辨率：${ScreenUtils.getAppScreenWidth()}x${ScreenUtils.getAppScreenHeight()}\n")
      append("状态栏高度：${BarUtils.getStatusBarHeight()}\n")
      append("导航栏高度：${BarUtils.getNavBarHeight()}\n")
    }

  private fun getCPUUsage(): Float {
    try {
      val reader = RandomAccessFile("/proc/stat", "r")
      var line = reader.readLine()
      var fields = line.split("\\s+")

      val idle1 = fields[4].toLong()
      val total1 = (1..7).map { fields[it].toLong() }.let {
        var i = 0L
        it.onEach { l ->
          i += l
        }
        i
      }

      SystemClock.sleep(1000)

      reader.seek(0)
      line = reader.readLine()
      reader.close()

      fields = line.split("\\s+")

      val idle2 = fields[4].toLong()
      val total2 = (1..7).map { fields[it].toLong() }.let {
        var i = 0L
        it.onEach { l ->
          i += l
        }
        i
      }

      return 100 * (1 - (idle2 - idle1) * 1.0f) / (total2 - total1)
    } catch (e: IOException) {
      e.printStackTrace()
      return 0f
    }
  }

  private fun getMemoryUsage(): String {
    val activityManager = requireContext().getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
    val memoryInfo = MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)
    return memoryInfo.toJson().formatJson()
  }

  private fun getProcesses(): List<ActivityManager.RunningAppProcessInfo> {
    val activityManager = requireContext().getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
    return activityManager.runningAppProcesses
  }
}