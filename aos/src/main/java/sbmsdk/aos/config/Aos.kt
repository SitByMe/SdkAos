package sbmsdk.aos.config

import android.app.Application
import android.view.View
import sbmsdk.aos.registerActivityLifecycle

/**
 * desc  : Aos Sdk 初始化类
 * date  : 2022/8/17
 *
 * @author zoulinheng
 */
class Aos private constructor() : IConfig {

  companion object {
    fun init(application: Application): Aos {
      return Aos().apply {
        application.registerActivityLifecycle { }
        AosConfig.setApp(application)
      }
    }
  }

  override fun setToastFunc(func: ToastFunction): IConfig {
    return AosConfig.setToastFunc(func)
  }

  override fun setGetUniqueDeviceIdFunc(creator: () -> String): IConfig {
    return AosConfig.setGetUniqueDeviceIdFunc(creator)
  }

  /**
   * 设置水印构造器
   */
  override fun setWatermarkCreator(createViewAction: () -> View?): IConfig {
    return AosConfig.setWatermarkCreator(createViewAction)
  }
}