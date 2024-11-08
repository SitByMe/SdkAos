package sbmsdk.aos.inputfilter

import android.text.InputFilter
import android.text.Spanned
import sbmsdk.aos.containsCnChar

/**
 * desc   : 不能输入汉字
 * date   : 2023/3/8
 *
 * @author zoulinheng
 */
class NotCnFilter : InputFilter {
  override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
    return if (source.toString().containsCnChar()) {
      ""
    } else {
      source
    }
  }
}