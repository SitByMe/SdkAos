package sbmsdk.aos.databinding.ktx

import android.view.View
import sbmsdk.aos.databinding.proxy.ClickProxy

/**
 * desc   :
 * date   : 2023/9/1
 * @author zoulinheng
 */

/**
 * 设置点击事件（Proxy）
 */
internal fun View.setOnClickListenerProxy(interval: Long? = null, onClickListener: View.OnClickListener) {
  if (interval == null) {
    setOnClickListener(ClickProxy(onClickListener))
  } else {
    setOnClickListener(ClickProxy(interval, onClickListener))
  }
}