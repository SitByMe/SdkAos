package sbmsdk.aos.config

import android.view.View

/**
 * desc   :
 * date   : 2024/8/2
 *
 * @author zoulinheng
 */
interface IConfig {
  /**
   * 设置水印构造器
   */
  fun setWatermarkCreator(createViewAction: () -> View?): IConfig

  /**
   * 设置Toast方法
   */
  fun setToastFunc(func: ToastFunction): IConfig

  /**
   * 设置获取设备唯一值方法
   */
  fun setGetUniqueDeviceIdFunc(creator: () -> String): IConfig
}