package sbmsdk.aos

import com.blankj.utilcode.util.ThreadUtils

/**
 * desc   :
 * date   : 2024/2/22
 *
 * @author zoulinheng
 */

/**
 * 执行在主线程
 */
fun runOnUiThread(runnable: Runnable) {
  ThreadUtils.runOnUiThread(runnable)
}