package sbmsdk.aos.builder

import android.content.res.ColorStateList
import androidx.annotation.ColorInt

/**
 * desc   : ColorStateList构造器
 * date   : 2023/4/4
 *
 * @author zoulinheng
 */
class ColorStateListBuilder {
  private val stateEnabled = android.R.attr.state_enabled   //是否可用
  private val stateFocused = android.R.attr.state_focused   //是否获得焦点
  private val statePressed = android.R.attr.state_pressed   //是否按下
  private val stateSelected = android.R.attr.state_selected //是否选中

  private lateinit var mColor: ColorStateList

  /**
   * 获取颜色
   */
  fun getColor(): ColorStateList = mColor

  private val stateList = mutableListOf<Pair<IntArray, @ColorInt Int>>()

  //默认状态下的背景
  @ColorInt
  private var normaColor: Int? = null

  /**
   * @param color     颜色
   * @param enabled   是否可用
   * @param focused   是否获得焦点
   * @param pressed   是否按下
   * @param selected  是否选中
   */
  fun addState(
    @ColorInt color: Int,
    enabled: Boolean? = null,
    focused: Boolean? = null,
    pressed: Boolean? = null,
    selected: Boolean? = null,
  ): ColorStateListBuilder {
    getStateSet(
      enabled = enabled,
      focused = focused,
      pressed = pressed,
      selected = selected
    ).let {
      if (it.isEmpty()) {
        normaColor = color
      } else {
        stateList.add(
          it to color
        )
      }
    }
    return this
  }

  internal fun build(): ColorStateListBuilder {
    val first = mutableListOf<IntArray>()
    val second = mutableListOf<Int>()
    stateList.onEach {
      first.add(it.first)
      second.add(it.second)
    }
    normaColor?.let {
      first.add(intArrayOf())
      second.add(it)
    }
    mColor = ColorStateList(first.toTypedArray(), second.toIntArray())
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