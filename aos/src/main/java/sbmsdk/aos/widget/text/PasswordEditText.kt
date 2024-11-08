package sbmsdk.aos.widget.text

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import sbmsdk.aos.R

/**
 * desc   : 密码EditText
 * date   : 2023/7/4
 *
 * @author zoulinheng
 */
class PasswordEditText(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : ActionEditText(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  private val typeEyeClose = 0
  private val typeEyeOpen = 1

  private var cType: Int = typeEyeClose
    set(value) {
      setType(value)
      field = value
    }
  private val onActionListener = OnClickListener {
    when (cType) {
      typeEyeClose -> cType = typeEyeOpen
      typeEyeOpen  -> cType = typeEyeClose
    }
  }

  private val icEyeOpen = ContextCompat.getDrawable(context, R.drawable.ic_eye_open)
  private val icEyeClose = ContextCompat.getDrawable(context, R.drawable.ic_eye_close)

  init {
    setActionListener(0, onActionListener)
  }

  private fun setType(type: Int) {
    when (type) {
      typeEyeClose -> {
        setActionDrawable(icEyeClose)
        editTextView.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
      }
      typeEyeOpen  -> {
        setActionDrawable(icEyeOpen)
        editTextView.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
      }
    }
    editTextView.setSelection(editTextView.text?.length ?: 0)
  }

  override fun getDefaultActionDrawable(): Drawable? {
    return when (cType) {
      typeEyeClose -> {
        editTextView.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
        ContextCompat.getDrawable(context, R.drawable.ic_eye_close)
      }
      typeEyeOpen  -> {
        editTextView.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        ContextCompat.getDrawable(context, R.drawable.ic_eye_open)
      }
      else         -> null
    }
  }
}