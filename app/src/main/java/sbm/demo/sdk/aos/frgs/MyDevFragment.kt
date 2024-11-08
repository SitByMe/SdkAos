package sbm.demo.sdk.aos.frgs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.*
import sbm.demo.sdk.aos.R

/**
 * desc   :
 * date   : 2024/6/11
 * @author zoulinheng
 */
class MyDevFragment : Fragment() {
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

  private val devInfo: CharSequence
    @SuppressLint("MissingPermission")
    get() = buildSpannedString {
      bold {
        append("设备信息\n")
      }
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

      bold {
        append("\n\n\n状态信息\n")
      }
      append("网络是否可用：${NetworkUtils.getWifiEnabled()}\n")
      append("网络状态：${NetworkUtils.getNetworkType()}\n")
      append("SSID：${NetworkUtils.getSSID()}\n")
      append("IP 地址：\n")
      append("    ${NetworkUtils.getIPAddress(true)}\n")
      append("    ${NetworkUtils.getIPAddress(false)}\n")

      bold {
        append("\n\n\n应用信息\n")
      }
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

      bold {
        append("\n\n\nCPU信息\n")
      }
    }
}