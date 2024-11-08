package sbmsdk.aos.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

/**
 * desc   : 默认空实现的ActivityLifecycleCallbacks
 * date   : 2022/12/2
 * @author zoulinheng
 */
interface EmptyActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
  override fun onActivityCreated(p0: Activity, p1: Bundle?) {
  }

  override fun onActivityStarted(p0: Activity) {
  }

  override fun onActivityResumed(p0: Activity) {
  }

  override fun onActivityPaused(p0: Activity) {
  }

  override fun onActivityStopped(p0: Activity) {
  }

  override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
  }

  override fun onActivityDestroyed(p0: Activity) {
  }
}