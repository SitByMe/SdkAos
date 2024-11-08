package sbmsdk.aos.debug.view

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import com.kongzue.stacklabelview.StackLayout
import sbmsdk.aos.setOnClickListenerProxy

/**
 * desc   :
 * date   : 2024/9/11
 *
 * @author zoulinheng
 */

fun StackLayout.addButton(text: CharSequence, onClickListener: View.OnClickListener) {
  val button = AppCompatButton(this.context).apply {
    layoutParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
    setText(text)
    isAllCaps = false
    setOnClickListenerProxy(onClickListener = onClickListener)
  }
  addView(button)
}