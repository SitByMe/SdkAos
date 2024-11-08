package sbmsdk.aos

import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.DimenRes

/**
 * desc   :
 * date   : 2023/7/11
 *
 * @author zoulinheng
 */

fun TextView.setTextSizeDimen(@DimenRes id: Int) {
  this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(id))
}

fun TextView.openScrollingMovement() {
  this.movementMethod = ScrollingMovementMethod.getInstance()
}