package sbmsdk.aos

/**
 * desc   :
 * date   : 2023/9/14
 *
 * @author zoulinheng
 */

inline fun CharSequence?.ifNullOrEmpty(defaultValue: () -> CharSequence): CharSequence =
  if (this.isNullOrEmpty()) defaultValue() else this

inline fun CharSequence?.ifNullOrBlank(defaultValue: () -> CharSequence): CharSequence =
  if (this.isNullOrBlank()) defaultValue() else this

inline fun String?.ifNullOrEmpty(defaultValue: () -> String): String =
  if (this.isNullOrEmpty()) defaultValue() else this

inline fun String?.ifNullOrBlank(defaultValue: () -> String): String =
  if (this.isNullOrBlank()) defaultValue() else this

inline fun String?.doIfNotNullOrBlank(action: (String) -> Unit) {
  if (!this.isNullOrBlank()) action.invoke(this)
}

inline fun String?.doIfNotNullOrEmpty(action: (String) -> Unit) {
  if (!this.isNullOrEmpty()) action.invoke(this)
}