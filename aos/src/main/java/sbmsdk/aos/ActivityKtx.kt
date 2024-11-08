package sbmsdk.aos

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import sbmsdk.aos.builder.BundleBuilder
import java.util.*

/**
 * desc  :
 * date  : 2022/8/17
 *
 * @author zoulinheng
 */

// Activity栈
internal val activityStack = LinkedList<Activity>()

// 所有打开的Activity列表
val activityList: List<Activity> get() = activityStack.toList()

// 顶层Activity
val topActivity: Activity get() = activityStack.last()

val topActivityOrNull: Activity? get() = activityStack.lastOrNull()

/**
 * 启动一个Activity
 *
 * @param bundleBuilder 携带参数
 */
inline fun <reified A : Activity> Context.startAct(noinline bundleBuilder: (BundleBuilder.() -> Unit)? = null) {
  val intent = Intent(this, A::class.java)
  bundleBuilder?.let {
    val builder = BundleBuilder()
    it.invoke(builder)
    intent.putExtras(builder.build())
  }
  startActivity(intent)
}

/**
 * 启动一个Activity
 *
 * @param bundleBuilder 携带参数
 */
inline fun <reified A : Activity> Fragment.startAct(noinline bundleBuilder: (BundleBuilder.() -> Unit)? = null) {
  requireContext().startAct<A>(bundleBuilder)
}

/**
 * 启动一个Activity
 *
 * @param pkg           包名
 * @param cls           activity名
 * @param bundleBuilder 携带参数
 */
fun Context.startAct(pkg: String, cls: String, bundleBuilder: (BundleBuilder.() -> Unit)? = null) {
  val intent = Intent()
  intent.component = ComponentName(pkg, cls)
  bundleBuilder.let {
    val builder = BundleBuilder()
    bundleBuilder?.invoke(builder)
    intent.putExtras(builder.build())
  }
  this.startActivity(intent)
}

/**
 * 关闭所有[clazz]类型的Activity
 *
 * @param clazz 指定Activity
 */
fun <A : Activity> finishActivity(clazz: Class<A>) {
  activityStack.removeAll {
    if (it.javaClass.name == clazz.name) {
      it.finish()
      true
    } else {
      false
    }
  }
}

/**
 * 关闭最近的一个[clazz]类型的Activity
 *
 * @param clazz 指定Activity
 */
fun <A : Activity> finishFirstActivity(clazz: Class<A>) {
  for (i in activityStack.count() - 1 downTo 0) {
    if (clazz.name == activityStack[i].javaClass.name) {
      activityStack.removeAt(i).finish()
      return
    }
  }
}

/**
 * 回退到指定Activity
 *
 * @param clazz 指定Activity
 */
fun <A : Activity> finishToActivity(clazz: Class<A>) {
  if (!activityStack.map { it.javaClass.name }.contains(clazz.name)) {
    return
  }
  for (i in activityStack.count() - 1 downTo 0) {
    if (clazz.name != activityStack[i].javaClass.name) {
      activityStack.removeAt(i).finish()
      return
    }
  }
}

/**
 * 从栈中finish掉所有满足条件[predicate]的Activity
 */
fun finishAllActivities(predicate: (Activity) -> Boolean) {
  for (i in activityStack.count() - 1 downTo 0) {
    if (predicate.invoke(activityStack[i])) {
      activityStack.removeAt(i).finish()
    }
  }
}

/**
 * 关闭所有Activity
 */
fun finishAllActivities() {
  activityStack.removeAll {
    it.finish()
    true
  }
}

/**
 * 获取[pkgName]指定App的launcherActivity
 *
 * @param pkgName 包名
 */
fun Context.getLauncherActivity(pkgName: String): String {
  if (pkgName.isBlank()) return ""
  val intent = Intent(Intent.ACTION_MAIN, null)
  intent.addCategory(Intent.CATEGORY_LAUNCHER)
  intent.setPackage(pkgName)
  val pm: PackageManager = this.packageManager
  val info = pm.queryIntentActivities(intent, 0)
  return info.firstOrNull()?.activityInfo?.name ?: ""
}