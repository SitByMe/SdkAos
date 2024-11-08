package sbmsdk.aos.debug.view

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import sbmsdk.aos.debug.builder.ButtonGroupBuilder
import sbmsdk.aos.getDimenInt
import sbmsdk.aos.setOnClickListenerProxy

/**
 * desc   :
 * date   : 2024/6/11
 * @author zoulinheng
 */

fun LinearLayoutCompat.addButton(text: CharSequence, interval: Long? = 1000, onClickListener: View.OnClickListener) {
  val button = AppCompatButton(this.context).apply {
    layoutParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
    setText(text)
    isAllCaps = false
    setOnClickListenerProxy(interval = interval, onClickListener = onClickListener)
  }
  addView(button)
}

fun LinearLayoutCompat.addTitle(text: CharSequence) {
  val textView = AppCompatTextView(this.context).apply {
    layoutParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
    setText(text)
    isAllCaps = false
    this.setPadding(context.getDimenInt(sbmsdk.aos.R.dimen.dp_4))
  }
  addView(textView)
}

fun LinearLayoutCompat.addButtonGroup(action: ButtonGroupBuilder.() -> Unit) {
  val builder = ButtonGroupBuilder(context)
  action.invoke(builder)
  this.addView(builder.createStackLayout())
}