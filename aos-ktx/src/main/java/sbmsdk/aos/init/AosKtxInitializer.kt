package sbmsdk.aos.init

import android.content.Context
import androidx.startup.Initializer
import sbmsdk.aos.dialogx.AosWidgetInitializer
import sbmsdk.aos.rxhttp.init.AosHttpInitializer

/**
 * desc   :
 * date   : 2024/10/28
 *
 * @author zoulinheng
 */
class AosKtxInitializer : Initializer<Unit> {
  override fun create(context: Context) {
  }

  override fun dependencies(): MutableList<Class<out Initializer<*>>> {
    return mutableListOf(
      AosWidgetInitializer::class.java,
      AosHttpInitializer::class.java,
    )
  }
}