package sbmsdk.aos.widget.text

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import sbmsdk.aos.R
import sbmsdk.aos.proxy.ClickProxy

/**
 * desc   : 支持设置行为按钮的EditText
 * date   : 2023/7/4
 *
 * @author zoulinheng
 */
open class ActionEditText(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayoutCompat(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  protected val drawableLeftView: AppCompatImageView = createDrawableLeftView()
  protected val editTextView: AppCompatEditText = createEditTextView()
  protected val actionView: AppCompatImageView = createActionView()

  private var drawableWidth: Int
  private var drawableHeight: Int

  private var bitmapLeft: Bitmap? = null
    set(value) {
      field = value
      drawableLeftView.setImageBitmap(field)
    }

  private var bitmapActionCache: Bitmap? = null
  private var bitmapAction: Bitmap? = null
    set(value) {
      field = value
      actionView.setImageBitmap(field)
    }

  private var drawablePadding: Int = 0
    set(value) {
      field = value
      val layoutParams = editTextView.layoutParams as MarginLayoutParams
      layoutParams.setMargins(field, 0, field, 0)
    }

  init {
    orientation = HORIZONTAL
    gravity = Gravity.CENTER_VERTICAL
    this.addView(drawableLeftView)
    this.addView(editTextView)
    this.addView(actionView)

    val a = context.obtainStyledAttributes(attrs, R.styleable.ActionEditText)
    val drawableSize = a.getDimensionPixelSize(R.styleable.ActionEditText_drawableSize, 0)
    val textSize = a.getDimensionPixelSize(R.styleable.ActionEditText_android_textSize, -1)
    if (textSize != -1) {
      editTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    drawableWidth = if (drawableSize <= 0) a.getDimensionPixelSize(R.styleable.ActionEditText_drawableWidth, 0) else drawableSize
    drawableHeight = if (drawableSize <= 0) a.getDimensionPixelSize(R.styleable.ActionEditText_drawableHeight, 0) else drawableSize
    if (drawableWidth == 0 && drawableHeight == 0) {
      drawableWidth = editTextView.textSize.toInt()
      drawableHeight = editTextView.textSize.toInt()
    }

    bitmapLeft = a.getDrawable(R.styleable.ActionEditText_android_drawableLeft)?.toBitmap(drawableWidth, drawableHeight)
    bitmapActionCache = (a.getDrawable(R.styleable.ActionEditText_drawableAction) ?: this.getDefaultActionDrawable())?.toBitmap(drawableWidth, drawableHeight)
    bitmapAction = bitmapActionCache

    if (a.hasValue(R.styleable.ActionEditText_android_drawablePadding)) {
      drawablePadding = a.getDimensionPixelSize(R.styleable.ActionEditText_android_drawablePadding, 0)
    }

    if (a.hasValue(R.styleable.ActionEditText_android_text)) {
      editTextView.setText(a.getText(R.styleable.ActionEditText_android_text))
    }

    if (a.hasValue(R.styleable.ActionEditText_android_hint)) {
      editTextView.hint = a.getText(R.styleable.ActionEditText_android_hint)
    }

    if (a.hasValue(R.styleable.ActionEditText_android_imeOptions)) {
      editTextView.imeOptions = a.getInt(R.styleable.ActionEditText_android_imeOptions, editTextView.imeOptions)
    }

    if (a.hasValue(R.styleable.ActionEditText_android_inputType)) {
      editTextView.inputType = a.getInt(R.styleable.ActionEditText_android_inputType, EditorInfo.TYPE_NULL)
    }

    a.recycle()
  }

  /**
   * 获取默认的行为图标
   */
  protected open fun getDefaultActionDrawable(): Drawable? {
    return ContextCompat.getDrawable(context, R.drawable.ig_close)
  }

  /**
   * 设置行为按钮的点击时间
   */
  fun setActionListener(interval: Long = 800, listener: OnClickListener) {
    actionView.setOnClickListener(ClickProxy(interval, listener))
  }

  /**
   * 获取输入的内容
   */
  fun getText(): Editable? {
    return editTextView.text
  }

  /**
   * 设置内容
   */
  fun setText(text: CharSequence?) {
    editTextView.setText(text)
  }

  /**
   * 设置左侧图标
   */
  fun setDrawableStart(drawable: Drawable?) {
    this.bitmapLeft = drawable?.toBitmap(drawableWidth, drawableHeight)
  }

  /**
   * 设置行为图标
   */
  fun setActionDrawable(drawable: Drawable?) {
    this.bitmapAction = drawable?.toBitmap(drawableWidth, drawableHeight)
  }

  /**
   * 重置行为图标
   */
  protected fun resetActionDrawable() {
    this.bitmapAction = this.bitmapActionCache
  }

  private fun createDrawableLeftView(): AppCompatImageView {
    val view = AppCompatImageView(context)
    view.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    return view
  }

  private fun createEditTextView(): AppCompatEditText {
    val view = AppCompatEditText(context)
    view.layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
      this.weight = 1f
    }
    view.background = null
    view.maxLines = 1
    view.isSingleLine = true
    return view
  }

  private fun createActionView(): AppCompatImageView {
    val view = AppCompatImageView(context)
    view.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    view.isClickable = true
    return view
  }

  object ActionEditTextAdapter {

    /*@JvmStatic
    @BindingAdapter("android:text")
    fun setText(view: ActionEditText, text: String?) {
      if (!TextUtils.equals(view.getText(), text)) {
        view.setText(text)
      }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "android:text", event = "textAttrChanged")
    fun getText(view: ActionEditText): String? {
      return view.getText()?.toString()
    }

    @JvmStatic
    @BindingAdapter("textAttrChanged")
    fun setTextAttrChanged(view: ActionEditText, listener: InverseBindingListener?) {
      view.editTextView.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
          listener?.onChange()
        }
      })
    }*/
  }
}