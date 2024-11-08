package sbmsdk.aos

import android.view.Window
import com.blankj.utilcode.util.BarUtils

/**
 * desc   :
 * date   : 2022/8/21
 *
 * @author zoulinheng
 */

/**
 * 设置状态栏为亮色/暗色
 *
 * @param isLightMode true:亮色   false:暗色
 */
fun Window.setStatusBarLightMode(isLightMode: Boolean) {
  BarUtils.setStatusBarLightMode(this, isLightMode)
}

/*---------- get size ----------*/

/**
 * 获取状态栏高度
 */
fun getStatusBarHeight(): Int {
  return BarUtils.getStatusBarHeight()
}

/**
 * 获取ActionBar高度
 */
fun getActionBarHeight(): Int {
  return BarUtils.getActionBarHeight()
}

/**
 * 获取NavigationBar高度
 */
fun getNavBarHeight(): Int {
  return BarUtils.getNavBarHeight()
}