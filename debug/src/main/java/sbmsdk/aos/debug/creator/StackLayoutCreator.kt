package sbmsdk.aos.debug.creator

import android.content.Context
import android.view.ViewGroup.LayoutParams
import com.kongzue.stacklabelview.StackLayout

/**
 * desc   :
 * date   : 2024/9/11
 *
 * @author zoulinheng
 */
class StackLayoutCreator(private val context: Context) : ICreator<StackLayout> {

  override fun create(): StackLayout {
    return StackLayout(context).apply {
      layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }
  }
}