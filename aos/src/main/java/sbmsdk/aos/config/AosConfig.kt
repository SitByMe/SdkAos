package sbmsdk.aos.config

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.MainThread
import com.blankj.utilcode.util.DeviceUtils
import sbmsdk.aos.config.AosConfig.getUniqueDeviceIdFun
import sbmsdk.aos.config.AosConfig.toastFun
import sbmsdk.aos.utils.WatermarkUtils

/**
 * desc   : 外部配置（library 需要集成方设置的配置）
 * date   : 2024/5/16
 *
 * @author zoulinheng
 *
 * @property toastFun             toast方法
 * @property getUniqueDeviceIdFun 获取设备唯一码的方法
 */
object AosConfig : IConfig {
  private var aAppOrNull: Application? = null

  //toast方法
  private var toastFun: ToastFunction = ToastFunction { context, text, duration -> Toast.makeText(context, text, duration).show() }

  //获取设备唯一码的方法
  private var getUniqueDeviceIdFun: () -> String = { DeviceUtils.getUniqueDeviceId() }

  /*---------- setter ----------*/

  fun setApp(application: Application): AosConfig {
    aAppOrNull = application
    return this
  }

  override fun setWatermarkCreator(createViewAction: () -> View?): IConfig {
    WatermarkUtils.setCreateViewAction(createViewAction)
    return this
  }

  override fun setToastFunc(func: ToastFunction): AosConfig {
    toastFun = func
    return this
  }

  override fun setGetUniqueDeviceIdFunc(creator: () -> String): IConfig {
    this.getUniqueDeviceIdFun = creator
    return this
  }

  /*---------- action ----------*/

  val app: Application
    get() = aAppOrNull ?: throw RuntimeException("aosManager.context is not initialized yet, Please call AosInit.init() first.")

  val context: Context get() = app.applicationContext

  @MainThread
  fun toast(context: Context, text: CharSequence?, duration: Int) = toastFun.toast(context, text, duration)

  val uniqueDeviceId: String get() = getUniqueDeviceIdFun.invoke()
}