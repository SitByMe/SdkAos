package sbmsdk.aos

import android.content.Context
import android.content.res.Resources
import androidx.annotation.DimenRes

/**
 * desc   :
 * date   : 2023/5/10
 *
 * @author zoulinheng
 */

fun Context.getDimenInt(@DimenRes id: Int): Int {
  return this.resources.getDimenInt(id)
}

fun Context.getDimenFloat(@DimenRes id: Int): Float {
  return this.resources.getDimenFloat(id)
}

fun Resources.getDimenInt(@DimenRes id: Int): Int {
  return cacheMap[id]?.toInt() ?: this.getDimensionPixelOffset(id).apply {
    cacheMap[id] = this.toFloat()
  }
}

fun Resources.getDimenFloat(@DimenRes id: Int): Float {
  return cacheMap[id] ?: this.getDimension(id).apply {
    cacheMap[id] = this
  }
}

private val cacheMap = mutableMapOf<Int, Float>()