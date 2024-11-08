package sbm.demo.sdk.aos.module.init.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.NestedScrollView
import sbm.demo.sdk.aos.module.init.AppInitDataModel
import sbmsdk.aos.getDimenInt

/**
 * desc   : 用来渲染 [AppInitDataModel] 的数据
 * date   : 2024/7/10
 *
 * @author zoulinheng
 *
 * 注意：只用作渲染 [AppInitDataModel] 的数据，和对此数据的回写
 */
class SAppInitLayout constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : NestedScrollView(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  private val dp8 = context.getDimenInt(sbmsdk.aos.R.dimen.dp_8)

  private val ll: LinearLayoutCompat

  init {
    ll = LinearLayoutCompat(context).apply {
      orientation = LinearLayoutCompat.VERTICAL
      layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
      setPadding(dp8, 0, dp8, 0)
    }
    addView(ll)
  }

  private var mData: AppInitDataModel? = null

  fun setData(data: AppInitDataModel) {
    data.init()
    this.mData = data
    initContentView()
  }

  private fun initContentView() {
    ll.removeAllViews()
    mData?.getInitItems()?.onEach { item ->
      val v = item.createItemView(ll.context, ll)
      val vLp = v.layoutParams
      val lp = LinearLayoutCompat.LayoutParams(vLp.width, vLp.height)
      val mTop = if (ll.childCount == 0) {
        dp8
      } else {
        0
      }
      lp.setMargins(0, mTop, 0, dp8)
      v.layoutParams = lp
      ll.addView(v)
    }
  }
}