package sbmsdk.aos.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import com.blankj.utilcode.util.Utils

/**
 * desc   :
 * date   : 2024/2/5
 *
 * @author zoulinheng
 */
object ScreenUtils {

  /**
   * 获取屏幕宽度（px）
   */
  fun getAppScreenWidth(): Int {
    val wm = Utils.getApp().applicationContext.getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return -1
    val point = Point()
    wm.defaultDisplay.getSize(point)
    return point.x
  }

  /**
   * 获取屏幕高度
   */
  fun getAppScreenHeight(): Int {
    val wm = Utils.getApp().applicationContext.getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return -1
    val point = Point()
    wm.defaultDisplay.getSize(point)
    return point.y
  }
}