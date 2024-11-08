package sbmsdk.aos.debug.builder

import android.content.Context
import android.view.View.OnClickListener
import androidx.appcompat.widget.AppCompatButton
import com.kongzue.stacklabelview.StackLayout
import sbmsdk.aos.debug.creator.ButtonCreator
import sbmsdk.aos.debug.creator.StackLayoutCreator

/**
 * desc   :
 * date   : 2024/9/11
 *
 * @author zoulinheng
 */
class ButtonGroupBuilder(private val context: Context) {

  private val btnCreators = mutableListOf<ButtonCreator>()

  fun add(text: CharSequence, onClickListener: OnClickListener): ButtonGroupBuilder = apply {
    btnCreators.add(ButtonCreator.Builder(context, text, onClickListener).build())
  }

  fun build(): List<AppCompatButton> {
    return btnCreators.map { it.create() }
  }

  fun createStackLayout(): StackLayout {
    return StackLayoutCreator(context).create().apply {
      btnCreators.onEach {
        this.addView(it.create())
      }
    }
  }
}