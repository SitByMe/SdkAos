package sbmsdk.aos.widget.text

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView

/**
 * desc   :
 * date   : 2023/4/18
 *
 * @author liuchang
 */
/**
 * Description :
 * PackageName : com.mrtrying.widget
 * Created by mrtrying on 2019/4/17 17:21.
 * e_mail : ztanzeyu@gmail.com
 */
class ExpandableTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatTextView(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  var minLine = 3
  private var isExpend = false //是否展开

  init {
    maxLines = minLine
    setOnClickListener {
      maxLines = if (!isExpend) {
        isExpend = true
        Int.MAX_VALUE
      } else {
        isExpend = false
        minLine
      }
      Log.i("text", "maxLines:$maxLines ")
      invalidate()
    }
  }
}