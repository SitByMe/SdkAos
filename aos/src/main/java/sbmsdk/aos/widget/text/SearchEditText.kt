package sbmsdk.aos.widget.text

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import sbmsdk.aos.R

/**
 * desc  : 搜索EditText
 * date  : 2022/7/17
 *
 * @author zoulinheng
 */
class SearchEditText(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : ClearEditText(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  private var onSearchListener: OnSearchListener? = null

  private val defStart = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.common_search)!!)

  init {
    editTextView.imeOptions = EditorInfo.IME_ACTION_SEARCH
    editTextView.setOnEditorActionListener { _, _, _ ->
      onSearchListener?.onSearch(editTextView.text?.toString())
      true
    }
    setDrawableStart(defStart)
  }

  /**
   * 设置搜索监听
   */
  fun setOnSearchListener(onSearchListener: OnSearchListener) {
    this.onSearchListener = onSearchListener
  }

  interface OnSearchListener {
    fun onSearch(text: String?)
  }
}