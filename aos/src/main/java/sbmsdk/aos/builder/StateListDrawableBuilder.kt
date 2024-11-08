package sbmsdk.aos.builder

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable

/**
 * desc   : StateListDrawable构造器
 * date   : 2023/4/3
 *
 * @author zoulinheng
 */
class StateListDrawableBuilder {
  private val stateEnabled = android.R.attr.state_enabled   //是否可用
  private val stateFocused = android.R.attr.state_focused   //是否获得焦点
  private val statePressed = android.R.attr.state_pressed   //是否按下
  private val stateSelected = android.R.attr.state_selected //是否选中

  private val mDrawable = StateListDrawable()

  /**
   * 获取drawable
   */
  fun getDrawable(): Drawable = mDrawable

  private val stateList = mutableListOf<Pair<IntArray, Drawable?>>()

  //默认状态下的背景
  private var normaDrawable: Drawable? = null

  /**
   * @param drawable  drawable
   * @param enabled   是否可用
   * @param focused   是否获得焦点
   * @param pressed   是否按下
   * @param selected  是否选中
   */
  fun addState(
    drawable: Drawable? = null,
    enabled: Boolean? = null,
    focused: Boolean? = null,
    pressed: Boolean? = null,
    selected: Boolean? = null,
  ): StateListDrawableBuilder {
    getStateSet(
      enabled = enabled,
      focused = focused,
      pressed = pressed,
      selected = selected
    ).let {
      if (it.isEmpty()) {
        normaDrawable = drawable
      } else {
        stateList.add(
          it to drawable
        )
      }
    }
    return this
  }

  internal fun build(): StateListDrawableBuilder {
    stateList.onEach {
      mDrawable.addState(it.first, it.second)
    }
    mDrawable.addState(getStateSet(), normaDrawable)
    return this
  }

  private fun getStateSet(
    enabled: Boolean? = null,
    focused: Boolean? = null,
    pressed: Boolean? = null,
    selected: Boolean? = null,
  ): IntArray {
    val intList = mutableListOf<Int>()
    if (enabled == true) intList.add(stateEnabled)
    if (focused == true) intList.add(stateFocused)
    if (pressed == true) intList.add(statePressed)
    if (selected == true) intList.add(stateSelected)
    return intList.toIntArray()
  }
}