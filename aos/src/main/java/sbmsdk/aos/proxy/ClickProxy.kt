package sbmsdk.aos.proxy

import android.view.View

/**
 * desc   : 防重点击代理类
 * date   : 2022/7/14
 *
 * @author zoulinheng
 */
class ClickProxy(private var interval: Long = 1000, private val onClickListener: View.OnClickListener) : View.OnClickListener {

  constructor(onClickListener: View.OnClickListener) : this(1000, onClickListener)

  init {
    if (interval < 0) {
      interval = 0
    }
  }

  private var preClickTime: Long = 0

  override fun onClick(v: View?) {
    val currentTimeMillis = System.currentTimeMillis()
    if (currentTimeMillis - preClickTime > interval) {
      preClickTime = currentTimeMillis
      onClickListener.onClick(v)
    }
  }
}