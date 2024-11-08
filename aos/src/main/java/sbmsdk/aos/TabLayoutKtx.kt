package sbmsdk.aos

import com.google.android.material.tabs.TabLayout

/**
 * desc   :
 * date   : 2023/9/1
 * @author zoulinheng
 */

fun TabLayout.setAutoFullTabMode() {
  this.tabMode = TabLayout.MODE_AUTO
  this.post {
    val fullWidth = this.measuredWidth
    val count = this.tabCount
    var usedWidth = 0
    repeat(count) {
      val view = this.getTabAt(it)?.view
      val width = view?.measuredWidth ?: 0
      usedWidth += width
    }
    val syWidth = fullWidth - usedWidth
    if (syWidth > 0) {
      val willInsertWidth = syWidth / count
      if (willInsertWidth > 0) {
        repeat(count) {
          val view = this.getTabAt(it)?.view
          view?.let { v ->
            v.setWidth(v.measuredWidth + willInsertWidth)
          }
        }
      }
    }
  }
}