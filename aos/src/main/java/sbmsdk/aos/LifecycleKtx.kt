package sbmsdk.aos

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * desc  :
 * date  : 2022/8/17
 *
 * @author zoulinheng
 */

internal fun Application.registerActivityLifecycle(
  onActivityCreated: ((Activity, Bundle?) -> Unit)? = null,
  onActivityStarted: ((Activity) -> Unit)? = null,
  onActivityResumed: ((Activity) -> Unit)? = null,
  onActivityPaused: ((Activity) -> Unit)? = null,
  onActivityStopped: ((Activity) -> Unit)? = null,
  onActivitySaveInstanceState: ((Activity, Bundle?) -> Unit)? = null,
  onActivityDestroyed: ((Activity) -> Unit)? = null,
) {
  this.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
      activityStack.add(activity)
      onActivityCreated?.invoke(activity, savedInstanceState)
    }

    override fun onActivityStarted(activity: Activity) {
      onActivityStarted?.invoke(activity)
    }

    override fun onActivityResumed(activity: Activity) {
      onActivityResumed?.invoke(activity)
    }

    override fun onActivityPaused(activity: Activity) {
      onActivityPaused?.invoke(activity)
    }

    override fun onActivityStopped(activity: Activity) {
      onActivityStopped?.invoke(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
      onActivitySaveInstanceState?.invoke(activity, outState)
    }

    override fun onActivityDestroyed(activity: Activity) {
      activityStack.remove(activity)
      onActivityDestroyed?.invoke(activity)
    }
  })
}