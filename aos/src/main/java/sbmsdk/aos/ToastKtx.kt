package sbmsdk.aos

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.Utils
import sbmsdk.aos.config.AosConfig

/**
 * desc  :
 * date  : 2022/8/17
 *
 * @author zoulinheng
 */

fun Context.showToastShort(text: CharSequence?) {
  toast(text, Toast.LENGTH_SHORT, false)
}

fun Context.showToastLong(text: CharSequence?) {
  toast(text, Toast.LENGTH_LONG, false)
}

fun Fragment.showToastShort(text: CharSequence?) {
  requireContext().toast(text, Toast.LENGTH_SHORT, false)
}

fun Fragment.showToastLong(text: CharSequence?) {
  requireContext().toast(text, Toast.LENGTH_LONG, false)
}

fun View.showToastShort(text: CharSequence?) {
  context.toast(text, Toast.LENGTH_SHORT, false)
}

fun View.showToastLong(text: CharSequence?) {
  context.toast(text, Toast.LENGTH_LONG, false)
}

fun showAppToastShort(text: CharSequence?) {
  Utils.getApp().toast(text, Toast.LENGTH_SHORT, false)
}

fun showAppToastLong(text: CharSequence?) {
  Utils.getApp().toast(text, Toast.LENGTH_LONG, false)
}

/*---------- debug ----------*/

fun Context.showToastShort(text: CharSequence?, onlyDebug: Boolean) {
  toast(text, Toast.LENGTH_SHORT, onlyDebug)
}

fun Context.showToastLong(text: CharSequence?, onlyDebug: Boolean) {
  toast(text, Toast.LENGTH_LONG, onlyDebug)
}

fun Fragment.showToastShort(text: CharSequence?, onlyDebug: Boolean) {
  requireContext().toast(text, Toast.LENGTH_SHORT, onlyDebug)
}

fun Fragment.showToastLong(text: CharSequence?, onlyDebug: Boolean) {
  requireContext().toast(text, Toast.LENGTH_LONG, onlyDebug)
}

fun View.showToastShort(text: CharSequence?, onlyDebug: Boolean) {
  context.toast(text, Toast.LENGTH_SHORT, onlyDebug)
}

fun View.showToastLong(text: CharSequence?, onlyDebug: Boolean) {
  context.toast(text, Toast.LENGTH_LONG, onlyDebug)
}

fun showAppToastShort(text: CharSequence?, onlyDebug: Boolean) {
  Utils.getApp().toast(text, Toast.LENGTH_SHORT, onlyDebug)
}

fun showAppToastLong(text: CharSequence?, onlyDebug: Boolean) {
  Utils.getApp().toast(text, Toast.LENGTH_LONG, onlyDebug)
}

/**
 * @param text      内容
 * @param duration  toast时长
 * @param onlyDebug 是否只有debug模式弹出
 */
private fun Context.toast(text: CharSequence?, duration: Int, onlyDebug: Boolean) {
  if (onlyDebug) return
  if (text.isNullOrEmpty()) return
  runOnUiThread {
    AosConfig.toast(this, text, duration)
  }
}
