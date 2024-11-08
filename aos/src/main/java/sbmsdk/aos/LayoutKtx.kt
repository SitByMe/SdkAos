package sbmsdk.aos

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * desc   :
 * date   : 2023/2/10
 *
 * @author zoulinheng
 */

fun Activity.createViewByLayoutId(@LayoutRes layoutId: Int, root: ViewGroup? = null, attachToRoot: Boolean = false): View {
  return layoutInflater.inflate(layoutId, root, root != null && attachToRoot)
}

fun Fragment.createViewByLayoutId(@LayoutRes layoutId: Int, root: ViewGroup? = null, attachToRoot: Boolean = false): View {
  return layoutInflater.inflate(layoutId, root, root != null && attachToRoot)
}

fun Context.createViewByLayoutId(@LayoutRes resId: Int, root: ViewGroup? = null, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(this).inflate(resId, root, root != null && attachToRoot)
}

fun View.createViewByLayoutId(@LayoutRes resId: Int, root: ViewGroup? = null, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(resId, root, root != null && attachToRoot)
}