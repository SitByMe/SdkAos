package sbm.demo.sdk.aos

import android.app.Application
import android.widget.Toast
import com.hjq.toast.Toaster
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import sbmsdk.aos.config.Aos

/**
 * desc   :
 * date   : 2024/2/6
 * @author zoulinheng
 */
class App : Application() {
  init {
    SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
      layout.setPrimaryColorsId(R.color.purple_200, android.R.color.white)
      ClassicsHeader(context)
    }
    SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> ClassicsFooter(context).setDrawableSize(20f) }
  }

  override fun onCreate() {
    super.onCreate()
    Toaster.init(this)
    Aos.init(this).setToastFunc { _, text, duration ->
      if (duration == Toast.LENGTH_LONG) {
        Toaster.showLong(text)
      } else {
        Toaster.showShort(text)
      }
    }.setGetUniqueDeviceIdFunc {
      "uniqueDeviceId"
    }
  }
}