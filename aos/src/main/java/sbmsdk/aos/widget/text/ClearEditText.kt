package sbmsdk.aos.widget.text

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.addTextChangedListener

/**
 * desc  : 带清除按钮的EditText
 * date  : 2022/7/17
 *
 * @author zoulinheng
 */
open class ClearEditText(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : ActionEditText(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  init {
    if (editTextView.text.isNullOrEmpty()) {
      setActionDrawable(null)
    }
    editTextView.addTextChangedListener {
      val text = it?.toString()
      if (text.isNullOrEmpty()) {
        setActionDrawable(null)
      } else {
        resetActionDrawable()
      }
    }
    setActionListener {
      setText("")
      onClearListener?.onClear()
    }
  }

  private var onClearListener: OnClearListener? = null

  /**
   * 设置内容清除监听
   */
  fun setOnClearListener(listener: OnClearListener) {
    this.onClearListener = listener
  }

  interface OnClearListener {
    fun onClear()
  }
}