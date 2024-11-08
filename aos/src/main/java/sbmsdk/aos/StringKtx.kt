package sbmsdk.aos

/**
 * desc   :
 * date   : 2023/9/13
 *
 * @author zoulinheng
 */
/**
 * 判断一个字符串中是否包含中文字符
 */
fun String.containsCnChar(): Boolean {
  if (this.isEmpty()) return false
  val chars = this.toCharArray()
  chars.onEach {
    if (it.isCnChar()) {
      return true
    }
  }
  return false
}

/**
 * 判断一个字符是否是中文字符
 */
fun Char.isCnChar(): Boolean {
  val ub = Character.UnicodeBlock.of(this)
  return Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS == ub ||
         Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS == ub ||
         Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS == ub ||
         Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT == ub ||
         Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A == ub ||
         Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B == ub
}
