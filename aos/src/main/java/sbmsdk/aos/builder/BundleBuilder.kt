package sbmsdk.aos.builder

import android.os.Bundle
import androidx.core.os.bundleOf

/**
 * desc   :
 * date   : 2023/9/6
 *
 * @author zoulinheng
 */
class BundleBuilder {
  private val pairMap = mutableMapOf<String, Any?>()

  fun put(key: String, value: Any?) {
    pairMap[key] = value
  }

  fun putAll(from: Map<String, Any?>) {
    pairMap.putAll(from)
  }

  fun putAll(extras: Bundle) {
    putAll(extras.keySet().associateWith {
      extras.get(it)
    })
  }

  fun build(): Bundle {
    val pairs = pairMap.map { it.key to it.value }.toTypedArray()
    return bundleOf(*pairs)
  }
}