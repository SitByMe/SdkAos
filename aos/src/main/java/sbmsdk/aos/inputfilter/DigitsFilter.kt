package sbmsdk.aos.inputfilter

import android.text.InputFilter
import android.text.Spanned

/**
 * desc   : 只能输入指定的字符
 * date   : 2024/1/31
 *
 * @author zoulinheng
 */
open class DigitsFilter(private val digits: String) : InputFilter {
  override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
    return check(source)
  }

  private fun check(text: CharSequence): CharSequence {
    return text.filter { digits.contains(it) }
  }
}