package sbmsdk.aos.dialogx.view.itemselect

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat

/**
 * desc   : 底部选择器的头部控价
 * date   : 2022/7/14
 * @author zoulinheng
 */
class PickerHeaderView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayoutCompat(context, attrs, defStyleAttr) {

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context) : this(context, null)

  private val leftView: AppCompatButton
  private val middleView: AppCompatTextView
  private val rightView: AppCompatButton

  init {
    orientation = HORIZONTAL
    gravity = Gravity.CENTER_VERTICAL

    leftView = AppCompatButton(context, attrs, defStyleAttr).apply {
      layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
    }
    leftView.setPadding(16, 8, 8, 8)
    leftView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
    leftView.setBackgroundResource(android.R.color.transparent)
    leftView.textSize = 15f

    middleView = AppCompatTextView(context, attrs, defStyleAttr).apply {
      layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f)
    }
    middleView.setPadding(8, 8, 8, 8)
    middleView.gravity = Gravity.CENTER
    middleView.isSingleLine = true
    middleView.ellipsize = TextUtils.TruncateAt.END
    middleView.textSize = 16f

    rightView = AppCompatButton(context, attrs, defStyleAttr).apply {
      layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
    }
    rightView.setPadding(8, 8, 16, 8)
    rightView.gravity = Gravity.END or Gravity.CENTER_VERTICAL
    rightView.setBackgroundResource(android.R.color.transparent)
    rightView.textSize = 15f

    addView(leftView)
    addView(middleView)
    addView(rightView)
  }

  /*---------- 设置文字 ----------*/
  fun setLeftText(text: CharSequence) {
    leftView.text = text
  }

  fun setMiddleText(text: CharSequence) {
    middleView.text = text
  }

  fun setRightText(text: CharSequence) {
    rightView.text = text
  }

  /*---------- 设置文字颜色 ----------*/
  fun setLeftTextColor(@ColorInt color: Int) {
    leftView.setTextColor(color)
  }

  fun setMiddleTextColor(@ColorInt color: Int) {
    middleView.setTextColor(color)
  }

  fun setRightTextColor(@ColorInt color: Int) {
    rightView.setTextColor(color)
  }

  /*---------- 设置事件 ----------*/
  fun setLeftClickListener(listener: OnClickListener?) {
    leftView.setOnClickListener(listener)
  }

  fun setRightClickListener(listener: OnClickListener?) {
    rightView.setOnClickListener(listener)
  }
}