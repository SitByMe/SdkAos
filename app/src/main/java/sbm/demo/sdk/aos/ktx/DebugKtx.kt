package sbm.demo.sdk.aos.ktx

import sbm.demo.sdk.aos.BuildConfig

/**
 * desc   :
 * date   : 2023/6/20
 * @author zoulinheng
 */

fun debugPrintln(any: Any?) {
  if (BuildConfig.DEBUG) {
    println(any)
  }
}