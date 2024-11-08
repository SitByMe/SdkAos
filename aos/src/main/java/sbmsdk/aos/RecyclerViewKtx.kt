package sbmsdk.aos

import androidx.recyclerview.widget.RecyclerView

/**
 * desc   :
 * date   : 2023/1/30
 *
 * @author zoulinheng
 */

/**
 * 判断RecyclerView是否已经滑动到顶部
 */
fun RecyclerView.isSlideToTop(): Boolean {
  return !this.canScrollVertically(-1)
}

/**
 * 判断RecyclerView中的最后一条数据是否可见
 */
fun RecyclerView.isSlideToBottom(): Boolean {
  return computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange()
}
