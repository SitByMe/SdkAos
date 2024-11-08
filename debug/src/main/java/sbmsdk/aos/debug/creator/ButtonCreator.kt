package sbmsdk.aos.debug.creator

import android.content.Context
import android.view.View.OnClickListener
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import sbmsdk.aos.setOnClickListenerProxy

/**
 * desc   : Button构造器
 * date   : 2024/9/11
 *
 * @author zoulinheng
 */
class ButtonCreator private constructor(private val context: Context, private val text: CharSequence, private val onClickListener: OnClickListener) : ICreator<AppCompatButton> {

  override fun create(): AppCompatButton {
    return AppCompatButton(this.context).apply {
      layoutParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
      text = this@ButtonCreator.text
      isAllCaps = false
      setOnClickListenerProxy(onClickListener = this@ButtonCreator.onClickListener)
    }
  }

  class Builder(private val context: Context, private val text: CharSequence, private val onClickListener: OnClickListener) {

    fun build(): ButtonCreator {
      return ButtonCreator(context = context, text = text, onClickListener = onClickListener)
    }
  }
}